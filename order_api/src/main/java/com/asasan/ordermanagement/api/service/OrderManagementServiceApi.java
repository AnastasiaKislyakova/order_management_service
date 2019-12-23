package com.asasan.ordermanagement.api.service;

import com.asasan.ordermanagement.api.dto.ItemAdditionParametersDto;
import com.asasan.ordermanagement.api.dto.OrderDto;
import com.asasan.ordermanagement.api.dto.OrderStatus;

import java.util.List;

public interface OrderManagementServiceApi {
    List<OrderDto> getOrders();
    OrderDto getOrderById(int orderId);
    OrderDto changeOrderStatus(int orderId, OrderStatus status);
    OrderDto addItemToOrder(Integer orderId, ItemAdditionParametersDto parametersDto);
    OrderDto addItemToOrder(ItemAdditionParametersDto parametersDto);
}
