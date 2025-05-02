package app.entities;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "supplier")
public class Supplier implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SupplierID")
    private int id;

    @Column(name = "Name", nullable = false, length = 50)
    private String name;

    @Column(name = "Representative", length = 50)
    private String representative;

    @Column(name = "Phone", length = 20)
    private String phone;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "AddressID")
    private Address address;

    public Supplier() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRepresentative() {
        return representative;
    }

    public void setRepresentative(String representative) {
        this.representative = representative;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", representative='" + representative + '\'' +
                ", phone='" + phone + '\'' +
                ", address=" + address +
                '}';
    }
}
