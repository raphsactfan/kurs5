package app.dto;

public class ProductSalesReportDTO {
    private String productName;
    private Long quantity;
    private Double total;

    public ProductSalesReportDTO(String productName, Long quantity, Double total) {
        this.productName = productName;
        this.quantity = quantity;
        this.total = total;
    }

    public String getProductName() {
        return productName;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Double getTotal() {
        return total;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

}
