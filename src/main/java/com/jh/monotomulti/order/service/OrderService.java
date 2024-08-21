package com.jh.monotomulti.order.service;

import com.jh.monotomulti.order.dto.CreateOrderReqDto;
import com.jh.monotomulti.order.dto.CreateOrderResDto;

import java.util.List;

public interface OrderService {
    CreateOrderResDto createOrder(int userId, List<CreateOrderReqDto> orderItems);
}
