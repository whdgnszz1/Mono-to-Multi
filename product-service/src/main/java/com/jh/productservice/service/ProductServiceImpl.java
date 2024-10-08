package com.jh.productservice.service;

import com.jh.common.dto.order.CreateOrderReqDto;
import com.jh.common.exception.BizRuntimeException;
import com.jh.productservice.entity.Product;
import com.jh.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public int getProductPrice(int productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BizRuntimeException("상품을 찾을 수 없습니다. 상품 ID: " + productId));

        return product.getPrice();
    }

    @Override
    public void updateStock(CreateOrderReqDto createOrderDto) {
        Product product = productRepository.findById(createOrderDto.getProductId())
                .orElseThrow(() -> new BizRuntimeException("상품을 찾을 수 없습니다. 상품 ID: " + createOrderDto.getProductId()));

        int newStock = product.getQuantity() - createOrderDto.getQuantity();
        if (newStock < 0) {
            log.error("재고가 부족합니다. 상품 ID: " + createOrderDto.getProductId());
            throw new BizRuntimeException("재고가 부족합니다.");
        }

        product.setQuantity(newStock);
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);
    }
}
