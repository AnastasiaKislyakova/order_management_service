package com.asasan.ordermanagement.app.entity;

import com.asasan.ordermanagement.api.dto.OrderDto;
import com.asasan.ordermanagement.api.dto.OrderStatus;
import com.asasan.ordermanagement.app.dto.CreateOrderDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status = OrderStatus.COLLECTING;

    private long totalCost = 0;

    private int totalAmount = 0;

    private String userName;

    // items[]

    public OrderEntity(CreateOrderDto createOrderDto) {
        Objects.requireNonNull(createOrderDto);
        this.totalCost = createOrderDto.getTotalCost();
        this.totalAmount = createOrderDto.getTotalAmount();
        this.userName = createOrderDto.getUserName();
    }

    public OrderDto toOrderDto() {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(orderId);
        //orderDto.setItems(items);
        orderDto.setStatus(status);
        orderDto.setTotalCost(totalCost);
        orderDto.setTotalAmount(totalAmount);
        orderDto.setUserName(userName);
        return orderDto;
    }
}