package app.dao;

import app.dto.ProductSalesReportDTO;
import app.entities.OrderProduct;
import app.entities.OrderProductId;
import app.entities.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, OrderProductId> {
    List<OrderProduct> findByOrder(OrderTable order);
}
