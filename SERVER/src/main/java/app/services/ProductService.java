package app.services;

import app.dao.ProductRepository;
import app.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getFilteredAndSorted(String search, String sortBy) {
        List<Product> products = productRepository.findAll();

        if (search != null && !search.isEmpty()) {
            String s = search.toLowerCase();
            products = products.stream()
                    .filter(p -> p.getName().toLowerCase().contains(s))
                    .collect(Collectors.toList());
        }

        if ("name".equalsIgnoreCase(sortBy)) {
            products.sort(Comparator.comparing(Product::getName));
        } else if ("price".equalsIgnoreCase(sortBy)) {
            products.sort(Comparator.comparingDouble(Product::getPrice));
        } else if ("quantity".equalsIgnoreCase(sortBy)) {
            products.sort(Comparator.comparingInt(Product::getQuantity));
        }

        return products;
    }

    public Product create(Product product) {
        validate(product);
        return productRepository.save(product);
    }

    public Product update(int id, Product updated) {
        Optional<Product> optional = productRepository.findById(id);
        if (optional.isEmpty()) {
            throw new RuntimeException("Товар не найден: " + id);
        }

        validate(updated);

        Product product = optional.get();
        product.setName(updated.getName());
        product.setQuantity(updated.getQuantity());
        product.setPrice(updated.getPrice());
        product.setSupplier(updated.getSupplier());
        product.setCategory(updated.getCategory());

        return productRepository.save(product);
    }

    public void delete(int id) {
        productRepository.deleteById(id);
    }

    private void validate(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()
                || product.getQuantity() <= 0
                || product.getPrice() <= 0
                || product.getSupplier() == null
                || product.getCategory() == null) {
            throw new RuntimeException("Все поля товара должны быть заполнены и корректны");
        }
    }

    public byte[] exportProductsToExcel() {
        List<Product> products = getFilteredAndSorted(null, null);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Товары");
            Row header = sheet.createRow(0);
            String[] columns = {"ID", "Название", "Количество", "Цена", "Категория", "Поставщик"};
            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }

            int rowIdx = 1;
            for (Product product : products) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(product.getId());
                row.createCell(1).setCellValue(product.getName());
                row.createCell(2).setCellValue(product.getQuantity());
                row.createCell(3).setCellValue(product.getPrice());
                row.createCell(4).setCellValue(product.getCategory().getName());
                row.createCell(5).setCellValue(product.getSupplier().getName());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при создании Excel: " + e.getMessage());
        }
    }
}
