package app.controllers;

import app.services.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "http://localhost:4200")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/categories")
    public Map<String, Integer> getCategoryStats() {
        return statisticsService.getCategoryStats();
    }

    @GetMapping("/suppliers")
    public Map<String, Integer> getSupplierStats() {
        return statisticsService.getSupplierStats();
    }

    @GetMapping("/top-users")
    public Map<String, Integer> getTopUsers() {
        return statisticsService.getTopUsersByOrders();
    }

    @GetMapping("/daily-sales")
    public Map<String, Integer> getDailySales() {
        return statisticsService.getSalesByDate();
    }

}
