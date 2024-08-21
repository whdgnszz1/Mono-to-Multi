package com.jh.orderservice.service;

import com.jh.common.dto.order.CreateOrderReqDto;
import com.jh.orderservice.dto.CreateOrderResDto;

import java.util.List;

public interface OrderService {
    CreateOrderResDto createOrder(int userId, List<CreateOrderReqDto> orderItems);
}
