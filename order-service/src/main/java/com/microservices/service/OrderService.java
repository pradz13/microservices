package com.microservices.service;

import com.microservices.dto.InventoryResponse;
import com.microservices.dto.OrderLineItemRequest;
import com.microservices.dto.OrderRequest;
import com.microservices.model.Order;
import com.microservices.model.OrderLineItem;
import com.microservices.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    private final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = Order.builder()
                .orderNumber(orderRequest.getOrderNumber())
                .orderLineItemList(orderRequest
                        .getOrderLineItemRequestList()
                        .stream()
                        .map(this::mapToOrderLineItem)
                        .collect(Collectors.toList()))
                .build();

        List<String> skuCodeList = order.getOrderLineItemList()
                .stream()
                .map(OrderLineItem::getSkuCode)
                .toList();

        //Call Inventory Service and place the order if product is in stock
        InventoryResponse[] inventoryResponseArray = webClient.get()
                .uri("http://localhost:8082/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodeList).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allProductsInStock = false;
        if(inventoryResponseArray != null && inventoryResponseArray.length > 0) {
            allProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);
        }

        if(allProductsInStock)
            orderRepository.save(order);
        else
            throw new IllegalStateException("Product is not in stock, please try again later.");
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
