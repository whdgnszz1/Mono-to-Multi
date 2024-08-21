package com.jh.monotomulti.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderResDto {
    private int orderId;
    private int userId;
    private Date orderDate;
    private int totalPrice;
}
