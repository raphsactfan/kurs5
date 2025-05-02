package app.services;

import app.dao.*;
import app.dto.OrderDTO;
import app.dto.OrderItemDTO;
import app.entities.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

@Service
public class OrderService {

    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderDetailsRepository orderDetailsRepository;
    @Autowired private OrderProductRepository orderProductRepository;
    @Autowired private AddressRepository addressRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;

    public String createOrder(OrderTable incomingOrder) {
        if (incomingOrder.getAddress() == null || incomingOrder.getUser() == null ||
                incomingOrder.getOrderProducts() == null || incomingOrder.getOrderProducts().isEmpty()) {
            throw new RuntimeException("Некорректные данные заказа");
        }

        Address savedAddress = addressRepository.save(incomingOrder.getAddress());

        User user = userRepository.findById(incomingOrder.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        OrderTable order = new OrderTable();
        order.setAddress(savedAddress);
        order.setUser(user);
        order.setQuantity(0);
        order.setTotalAmount(0.0);
        order.setOrderProducts(null);
        OrderTable savedOrder = orderRepository.save(order);

        OrderDetails details = new OrderDetails();
        details.setOrder(savedOrder);
        details.setDateTime(LocalDateTime.now());
        orderDetailsRepository.save(details);

        int totalQty = 0;
        double totalAmount = 0;

        for (OrderProduct op : incomingOrder.getOrderProducts()) {
            Product product = productRepository.findById(op.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Товар не найден"));

            int qty = op.getQuantity();
            if (product.getQuantity() < qty) {
                throw new RuntimeException("Недостаточно товара: ID = " + product.getId());
            }

            product.setQuantity(product.getQuantity() - qty);
            productRepository.save(product);

            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(savedOrder);
            orderProduct.setOrderId(savedOrder.getOrderId());
            orderProduct.setProduct(product);
            orderProduct.setProductId(product.getId());
            orderProduct.setQuantity(qty);
            orderProductRepository.save(orderProduct);

            totalQty += qty;
            totalAmount += qty * product.getPrice();
        }

        savedOrder.setQuantity(totalQty);
        savedOrder.setTotalAmount(totalAmount);
        orderRepository.save(savedOrder);

        return "Заказ успешно создан";
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream().map(order -> {
            String login = order.getUser().getLogin();
            String address = order.getAddress().getCountry() + ", " +
                    order.getAddress().getCity() + ", " +
                    order.getAddress().getStreet();

            List<OrderItemDTO> items = order.getOrderProducts().stream()
                    .map(op -> new OrderItemDTO(op.getProduct().getName(),
                            op.getProduct().getPrice(),
                            op.getQuantity()))
                    .toList();

            OrderDetails details = orderDetailsRepository.findById(order.getOrderId()).orElse(null);
            String dateTime = (details != null) ? details.getDateTime().toString() : "";

            return new OrderDTO(order.getOrderId(), login, address, items, order.getQuantity(), order.getTotalAmount(), dateTime);
        }).toList();
    }

    public void deleteOrder(int id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Заказ не найден");
        }
        orderRepository.deleteById(id);
    }

    public byte[] generateReceipt(int orderId) throws Exception {
        OrderTable order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Заказ не найден"));

        OrderDetails details = orderDetailsRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Детали заказа не найдены"));

        List<OrderProduct> products = orderProductRepository.findByOrder(order);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            BaseFont bf = BaseFont.createFont("fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font titleFont = new Font(bf, 16, Font.BOLD);
            Font normalFont = new Font(bf, 12);

            document.add(new Paragraph("Чек заказа №" + order.getOrderId(), titleFont));
            document.add(new Paragraph("Дата: " + details.getDateTime(), normalFont));
            document.add(new Paragraph("Пользователь: " + order.getUser().getLogin(), normalFont));
            document.add(new Paragraph("Адрес: " + order.getAddress().getCountry() + ", " +
                    order.getAddress().getCity() + ", " + order.getAddress().getStreet(), normalFont));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            Stream.of("Товар", "Цена", "Кол-во", "Сумма").forEach(header -> {
                PdfPCell cell = new PdfPCell(new Phrase(header, normalFont));
                table.addCell(cell);
            });

            for (OrderProduct op : products) {
                Product p = op.getProduct();
                table.addCell(new Phrase(p.getName(), normalFont));
                table.addCell(new Phrase(p.getPrice() + "₽", normalFont));
                table.addCell(new Phrase(String.valueOf(op.getQuantity()), normalFont));
                table.addCell(new Phrase((p.getPrice() * op.getQuantity()) + "₽", normalFont));
            }

            document.add(table);
            document.add(new Paragraph("Всего товаров: " + order.getQuantity(), normalFont));
            document.add(new Paragraph("Итоговая сумма: " + order.getTotalAmount() + "₽", normalFont));
            document.close();

            return baos.toByteArray();
        }
    }

    public byte[] exportOrdersToExcel() {
        List<OrderDTO> orders = getAllOrders();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("История заказов");
            Row header = sheet.createRow(0);
            String[] columns = {
                    "ID заказа", "Пользователь", "Дата", "Адрес",
                    "Товар", "Цена", "Кол-во", "Сумма"
            };
            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }

            int rowIdx = 1;
            for (OrderDTO order : orders) {
                for (OrderItemDTO item : order.getItems()) {
                    Row row = sheet.createRow(rowIdx++);
                    row.createCell(0).setCellValue(order.getOrderId());
                    row.createCell(1).setCellValue(order.getLogin());
                    row.createCell(2).setCellValue(order.getDateTime());
                    row.createCell(3).setCellValue(order.getAddress());
                    row.createCell(4).setCellValue(item.getProductName());
                    row.createCell(5).setCellValue(item.getPrice());
                    row.createCell(6).setCellValue(item.getQuantity());
                    row.createCell(7).setCellValue(item.getPrice() * item.getQuantity());
                }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при создании Excel: " + e.getMessage());
        }
    }

}
