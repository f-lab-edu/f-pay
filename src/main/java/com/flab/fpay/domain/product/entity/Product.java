package com.flab.fpay.domain.product.entity;

import com.flab.fpay.domain.common.entity.BaseEntity;
import com.flab.fpay.domain.product.enums.ProductCategory;
import com.flab.fpay.domain.product.enums.ProductStatus;
import jakarta.persistence.*;

@Entity
@Table(
        name = "product"
)
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "vendor_id")
    private long vendorId;

    @Column(name = "name")
    private String name;

    @Column(name = "serial_no")
    private String serialNo;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private int price;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private ProductCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductStatus status;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    public Product() {
    }

    public Product(long vendorId, String name, String serialNo, int quantity, int price, ProductCategory category, ProductStatus status, String imgUrl, boolean isDeleted) {
        this.vendorId = vendorId;
        this.name = name;
        this.serialNo = serialNo;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
        this.status = status;
        this.imgUrl = imgUrl;
        this.isDeleted = isDeleted;
    }

    public long getId() {
        return id;
    }

    public long getVendorId() {
        return vendorId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
