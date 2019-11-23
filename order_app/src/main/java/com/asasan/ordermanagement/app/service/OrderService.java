package com.asasan.ordermanagement.app.service;

import com.asasan.ordermanagement.api.dto.ItemAdditionParametersDto;
import com.asasan.ordermanagement.api.dto.OrderDto;
import com.asasan.ordermanagement.api.dto.OrderStatus;
import com.asasan.ordermanagement.app.dto.CreateOrderDto;
import com.asasan.ordermanagement.app.entity.OrderEntity;

import java.util.List;

public interface OrderService {

    List<OrderDto> getOrders();
    OrderDto getOrderById(int orderId);
    OrderDto changeOrderStatus(int orderId, OrderStatus status);
    OrderDto addItemToOrder(Integer orderId, ItemAdditionParametersDto parametersDto);
   // OrderDto createOrder(CreateOrderDto createOrderDto);
}
