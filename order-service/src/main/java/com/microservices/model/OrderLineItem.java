package com.microservices.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ORDER_LINE_ITEM")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
}
