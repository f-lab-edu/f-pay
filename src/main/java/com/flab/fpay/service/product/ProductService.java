package com.flab.fpay.service.product;

import com.flab.fpay.domain.product.entity.Product;

public interface ProductService {
    Product getProductById(long id);

    void decreaseQuantity(long productId);

    void increaseQuantity(long productId);

    int getTotalPrice(Product product, int quantity);
}
