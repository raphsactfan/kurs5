package app.entities;

import java.io.Serializable;
import java.util.Objects;

public class OrderProductId implements Serializable {
    private int orderId;
    private int productId;

    public OrderProductId() {
    }

    public OrderProductId(int orderId, int productId) {
        this.orderId = orderId;
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderProductId)) return false;
        OrderProductId that = (OrderProductId) o;
        return orderId == that.orderId && productId == that.productId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, productId);
    }
}
