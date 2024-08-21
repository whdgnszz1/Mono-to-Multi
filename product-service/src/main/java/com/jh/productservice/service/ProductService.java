package com.jh.productservice.service;

import com.jh.common.dto.order.CreateOrderReqDto;

public interface ProductService {

    void updateStock(CreateOrderReqDto createOrderDto);

    int getProductPrice(int productId);
}