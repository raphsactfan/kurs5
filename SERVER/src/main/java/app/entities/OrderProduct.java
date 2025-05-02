package app.entities;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "orderproduct")
@IdClass(OrderProductId.class)
public class OrderProduct implements Serializable {

    @Id
    @Column(name = "OrderID")
    private int orderId;

    @Id
    @Column(name = "ProductID")
    private int productId;

    @ManyToOne
    @JoinColumn(name = "OrderID", insertable = false, updatable = false)
    private OrderTable order;

    @ManyToOne
    @JoinColumn(name = "ProductID", insertable = false, updatable = false)
    private Product product;

    @Column(name = "Quantity")
    private int quantity;

    public OrderProduct() {}

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public OrderTable getOrder() {
        return order;
    }

    public void setOrder(OrderTable order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
