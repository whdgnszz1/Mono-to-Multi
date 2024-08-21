package com.jh.monotomulti.order.service;

import com.jh.monotomulti.common.exception.BizRuntimeException;
import com.jh.monotomulti.order.dto.CreateOrderReqDto;
import com.jh.monotomulti.order.dto.CreateOrderResDto;
import com.jh.monotomulti.order.entity.Order;
import com.jh.monotomulti.order.entity.OrderItem;
import com.jh.monotomulti.order.repository.OrderItemRepository;
import com.jh.monotomulti.order.repository.OrderRepository;
import com.jh.monotomulti.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderServiceImpl implements OrderService {

    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public CreateOrderResDto createOrder(int userId, List<CreateOrderReqDto> orderItems) {
        try {
            Order order = createAndSaveOrder(userId);
            int totalPrice = calculateTotalPrice(order, orderItems);
            updateOrderTotalPrice(order, totalPrice);
            return new CreateOrderResDto(order.getId(), userId, order.getOrderDate(), totalPrice);
        } catch (DataAccessException e) {
            log.error("데이터베이스 접근 중 오류 발생", e);
            throw new BizRuntimeException("주문 처리 중 데이터베이스 오류가 발생했습니다.", e);
        } catch (BizRuntimeException e) {
            log.error("주문 처리 중 비즈니스 로직 오류 발생", e);
            throw e;  // 이미 처리된 BizRuntimeException은 다시 던집니다.
        } catch (Exception e) {
            log.error("주문 처리 중 예기치 않은 오류 발생", e);
            throw new BizRuntimeException("주문 처리 중 예기치 않은 오류가 발생했습니다.", e);
        }
    }

    private Order createAndSaveOrder(int userId) {
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderDate(new Date());
        order.setTotalPrice(0);

        return saveOrder(order);
    }

    private Order saveOrder(Order order) {
        try {
            return orderRepository.save(order);
        } catch (DataAccessException e) {
            log.error("주문 저장 중 데이터베이스 오류 발생", e);
            throw new BizRuntimeException("주문 저장에 실패했습니다.", e);
        } catch (Exception e) {
            log.error("주문 저장 중 예기치 않은 오류 발생", e);
            throw new BizRuntimeException("주문 저장 중 예기치 않은 오류가 발생했습니다.", e);
        }
    }

    private int calculateTotalPrice(Order order, List<CreateOrderReqDto> orderItems) {
        List<CompletableFuture<Integer>> futureList = orderItems.stream()
                .map(item -> CompletableFuture.supplyAsync(() -> processOrderItem(order.getId(), item)))
                .toList();

        try {
            return futureList.stream()
                    .mapToInt(future -> {
                        try {
                            return future.get(30, TimeUnit.SECONDS);
                        } catch (InterruptedException | ExecutionException | TimeoutException e) {
                            log.error("주문 가격 계산 실패", e);
                            throw new BizRuntimeException("주문 가격 계산에 실패했습니다.", e);
                        }
                    })
                    .sum();
        } catch (BizRuntimeException e) {
            throw e;
        }
    }

    private int processOrderItem(int orderId, CreateOrderReqDto item) {
        OrderItem orderItem = createAndSaveOrderItem(orderId, item);
        int productPrice = productService.getProductPrice(item.getProductId());
        updateProductStock(item);
        return productPrice * item.getQuantity();
    }

    private OrderItem createAndSaveOrderItem(int orderId, CreateOrderReqDto item) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(orderId);
        orderItem.setProductId(item.getProductId());
        orderItem.setQuantity(item.getQuantity());

        return saveOrderItem(orderItem);
    }

    private OrderItem saveOrderItem(OrderItem orderItem) {
        try {
            return orderItemRepository.save(orderItem);
        } catch (DataAccessException e) {
            log.error("주문 항목 저장 중 데이터베이스 오류 발생", e);
            throw new BizRuntimeException("주문 항목 저장에 실패했습니다.", e);
        } catch (Exception e) {
            log.error("주문 항목 저장 중 예기치 않은 오류 발생", e);
            throw new BizRuntimeException("주문 항목 저장 중 예기치 않은 오류가 발생했습니다.", e);
        }
    }

    private void updateOrderTotalPrice(Order order, int totalPrice) {
        try {
            order.setTotalPrice(totalPrice);
            orderRepository.save(order);
        } catch (DataAccessException e) {
            log.error("주문 가격 업데이트 중 데이터베이스 오류 발생", e);
            throw new BizRuntimeException("주문 가격 업데이트에 실패했습니다.", e);
        } catch (Exception e) {
            log.error("주문 가격 업데이트 중 예기치 않은 오류 발생", e);
            throw new BizRuntimeException("주문 가격 업데이트 중 예기치 않은 오류가 발생했습니다.", e);
        }
    }

    private void updateProductStock(CreateOrderReqDto item) {
        try {
            productService.updateStock(item);
        } catch (BizRuntimeException e) {
            log.error("재고 업데이트 중 오류 발생", e);
            throw e;
        } catch (Exception e) {
            log.error("재고 업데이트 중 예기치 않은 오류 발생", e);
            throw new BizRuntimeException("재고 업데이트 중 예기치 않은 오류가 발생했습니다.", e);
        }
    }
}
