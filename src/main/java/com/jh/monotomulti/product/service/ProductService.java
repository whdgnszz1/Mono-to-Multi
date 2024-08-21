package com.jh.monotomulti.product.service;

import com.jh.monotomulti.order.dto.CreateOrderReqDto;

public interface ProductService {

    void updateStock(CreateOrderReqDto createOrderDto);

    int getProductPrice(int productId);
}