package com.app.spring.primefaces.entities;

import com.app.spring.primefaces.enums.InventoryStatus;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
@Entity
@Table(name = "productos")
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    private String code;
    private String name;
    private String description;
    private String image;
    private double price;
    private String category;
    private int quantity;
    private InventoryStatus inventoryStatus;
    private int rating;
    private List<Order> orders;

    @Column(name = "create_at")
    @Temporal(TemporalType.DATE)
    private Date createAt;

    // ----- RELACIÓN MANY-TO-ONE -----
    @ManyToOne // Un Producto pertenece a una Categoría
    @JoinColumn(name = "categoria_id") // Nombre de la columna FK en la tabla productos
    private Categoria categoria;
    @PrePersist
    public void prePersist() {
        this.createAt = new Date();
    }
    public Product() {
    }

    public Product(int id, String code, String name, String description, String image, double price, String category, int quantity,
                   InventoryStatus inventoryStatus, int rating) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.category = category;
        this.quantity = quantity;
        this.inventoryStatus = inventoryStatus;
        this.rating = rating;
    }

    @Override
    public Product clone() {
        return new Product(getId(), getCode(), getName(), getDescription(), getImage(), getPrice(), getCategory(), getQuantity(),
                getInventoryStatus(), getRating());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public InventoryStatus getInventoryStatus() {
        return inventoryStatus;
    }

    public void setInventoryStatus(InventoryStatus inventoryStatus) {
        this.inventoryStatus = inventoryStatus;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public List<Order> getOrders() {
        return this.orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Product other = (Product) obj;
        return Objects.equals(getCode(), other.getCode());
    }
}
