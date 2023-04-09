package com.microservices.service;

import com.microservices.dto.OrderLineItemRequest;
import com.microservices.dto.OrderRequest;
import com.microservices.model.Order;
import com.microservices.model.OrderLineItem;
import com.microservices.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = Order.builder()
                .orderNumber(orderRequest.getOrderNumber())
                .orderLineItemList(orderRequest
                        .getOrderLineItemRequestList()
                        .stream()
                        .map(this::mapToOrderLineItem)
                        .collect(Collectors.toList()))
                        .build();

        orderRepository.save(order);
        log.info("Order saved successfully");
    }

    private OrderLineItem mapToOrderLineItem(OrderLineItemRequest orderLineItemRequest) {
        return OrderLineItem.builder()
                .skuCode(orderLineItemRequest.getSkuCode())
                .price(orderLineItemRequest.getPrice())
                .quantity(orderLineItemRequest.getQuantity())
                .build();

    }
}
