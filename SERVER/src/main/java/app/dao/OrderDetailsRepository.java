package app.dao;

import app.entities.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {
    List<OrderDetails> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);
}

