package app.services;

import app.dao.OrderDetailsRepository;
import app.dto.ProductSalesReportDTO;
import app.entities.OrderDetails;
import app.entities.OrderProduct;
import app.entities.OrderTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ReportService {

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    public List<ProductSalesReportDTO> getReportByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        List<OrderDetails> detailsInDay = orderDetailsRepository.findByDateTimeBetween(startOfDay, endOfDay);
        Map<String, ProductSalesReportDTO> resultMap = new LinkedHashMap<>();

        for (OrderDetails details : detailsInDay) {
            OrderTable order = details.getOrder();
            List<OrderProduct> products = order.getOrderProducts();

            for (OrderProduct op : products) {
                String name = op.getProduct().getName();
                double price = op.getProduct().getPrice();
                int qty = op.getQuantity();
                double sum = price * qty;

                resultMap.compute(name, (key, existing) -> {
                    if (existing == null) {
                        return new ProductSalesReportDTO(name, (long) qty, sum);
                    } else {
                        existing.setQuantity(existing.getQuantity() + qty);
                        existing.setTotal(existing.getTotal() + sum);
                        return existing;
                    }
                });
            }
        }

        return new ArrayList<>(resultMap.values());
    }
}
