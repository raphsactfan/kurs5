package app.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "orderdetails")
public class OrderDetails implements Serializable {

    @Id
    @Column(name = "OrderID")
    private int orderId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "OrderID")
    private OrderTable order;

    @Column(name = "DateTime")
    private LocalDateTime dateTime;

    public OrderDetails() {
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public OrderTable getOrder() {
        return order;
    }

    public void setOrder(OrderTable order) {
        this.order = order;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "orderId=" + orderId +
                ", dateTime=" + dateTime +
                '}';
    }
}
