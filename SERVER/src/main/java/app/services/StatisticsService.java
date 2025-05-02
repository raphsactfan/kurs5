package app.services;

import app.dao.OrderRepository;
import app.dao.ProductRepository;
import app.entities.OrderTable;
import app.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import app.dao.OrderDetailsRepository;

import java.util.*;

@Service
public class StatisticsService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;


    public Map<String, Integer> getCategoryStats() {
        List<Product> products = productRepository.findAll();
        Map<String, Integer> stats = new HashMap<>();

        for (Product p : products) {
            String category = p.getCategory().getName();
            stats.put(category, stats.getOrDefault(category, 0) + 1);
        }

        return stats;
    }

    public Map<String, Integer> getSupplierStats() {
        List<Product> products = productRepository.findAll();
        Map<String, Integer> stats = new HashMap<>();

        for (Product p : products) {
            String supplier = p.getSupplier().getName();
            stats.put(supplier, stats.getOrDefault(supplier, 0) + 1);
        }

        return stats;
    }

    public Map<String, Integer> getTopUsersByOrders() {
        List<OrderTable> orders = orderRepository.findAll();
        Map<String, Integer> stats = new HashMap<>();

        for (OrderTable order : orders) {
            String login = order.getUser().getLogin();
            stats.put(login, stats.getOrDefault(login, 0) + 1);
        }

        return stats.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(5)
                .collect(LinkedHashMap::new,
                        (map, entry) -> map.put(entry.getKey(), entry.getValue()),
                        LinkedHashMap::putAll);
    }

    public Map<String, Integer> getSalesByDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Map<String, Integer> stats = new TreeMap<>();

        orderDetailsRepository.findAll().forEach(details -> {
            String date = details.getDateTime().toLocalDate().format(formatter);
            stats.put(date, stats.getOrDefault(date, 0) + 1);
        });

        return stats;
    }
}
