package com.flab.fpay.service.product.impl;

import com.flab.fpay.domain.product.entity.Product;
import com.flab.fpay.repository.product.ProductJpaRepository;
import com.flab.fpay.service.product.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductJpaRepository productJpaRepository;

    public ProductServiceImpl(ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    public Product getProductById(long id) {
        return productJpaRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void decreaseQuantity(long productId) {
        Product product = productJpaRepository.findByIdWithPessimisticLock(productId);

        if(product.getQuantity() - 1 < 0) {
            throw new RuntimeException("product quantity cannot be less than zero");
        }

        product.setQuantity(product.getQuantity() - 1);
    }

    /**
     * @param productId : 검증된 상품 ID로 가정
     * 결제 & 주문 실패 관련 예외 발생 시 사용되므로 별도의 트랜잭션으로 동작한다.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void increaseQuantity(long productId) {
        Product product = productJpaRepository.findByIdWithPessimisticLock(productId);

        product.setQuantity(product.getQuantity() + 1);
    }

    @Override
    public int getTotalPrice(Product product, int quantity) {
        return product.getPrice() * quantity;
    }
}
