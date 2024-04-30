package com.flab.fpay.service.product.impl;

import com.flab.fpay.domain.product.entity.Product;
import com.flab.fpay.domain.product.enums.ProductCategory;
import com.flab.fpay.domain.product.enums.ProductStatus;
import com.flab.fpay.repository.product.ProductJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class ProductServiceImplTest {
    @Autowired
    ProductServiceImpl productService;
    @Autowired
    ProductJpaRepository productJpaRepository;

    long productId;

    @BeforeEach
    public void before() {
        int productQuantity = 100;

        Product product = new Product(1, "상품", "일렬번호", productQuantity, 10_000, ProductCategory.TICKET, ProductStatus.ON_SALE, "imageUrl", false);
        this.productId = productJpaRepository.save(product).getId();
    }

    @AfterEach
    public void after() {
        productJpaRepository.deleteAll();
    }

    @Test
    @DisplayName("재고 감소")
    public void decreaseQuantity() {
        // given & when
        productService.decreaseQuantity(productId);

        // then
        Product product = productJpaRepository.findById(1L).get();

        assertThat(product.getQuantity()).isEqualTo(99);
    }

    @Test
    @DisplayName("동시에 100개의 요청")
    public void decreaseQuantityMultiThread() throws InterruptedException {
        // given & when
        int threadCount = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    productService.decreaseQuantity(productId);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        Product product = productJpaRepository.findById(productId).get();

        assertThat(product.getQuantity()).isZero();
    }

}