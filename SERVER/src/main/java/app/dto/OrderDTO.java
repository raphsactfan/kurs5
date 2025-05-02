package app.dto;

import java.util.List;

public class OrderDTO {
    private int orderId;
    private String login;
    private String address;
    private List<OrderItemDTO> items;
    private int totalQuantity;
    private double totalAmount;

    private String dateTime;

    public OrderDTO(int orderId, String login, String address,
                    List<OrderItemDTO> items, int totalQuantity, double totalAmount, String dateTime) {
        this.orderId = orderId;
        this.login = login;
        this.address = address;
        this.items = items;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
        this.dateTime = dateTime;
    }

    public OrderDTO() {}

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
