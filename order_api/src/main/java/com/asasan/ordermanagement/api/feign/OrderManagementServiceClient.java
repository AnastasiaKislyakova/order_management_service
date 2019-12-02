package com.asasan.ordermanagement.api.feign;

import com.asasan.ordermanagement.api.dto.ItemAdditionParametersDto;
import com.asasan.ordermanagement.api.dto.OrderDto;
import com.asasan.ordermanagement.api.dto.OrderStatus;
import com.asasan.ordermanagement.api.service.OrderManagementServiceApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@FeignClient("${order.management.service.name}/api/orders")
public interface OrderManagementServiceClient extends OrderManagementServiceApi {

    @GetMapping()
    List<OrderDto> getOrders();

    @GetMapping(value = "{orderId}")
    OrderDto getOrderById(@PathVariable int orderId);

    @PostMapping(value = "{orderId}/item")
    OrderDto addItemToOrder(@PathVariable Integer orderId, ItemAdditionParametersDto parametersDto);

    @PutMapping(value = "{orderId}/status/{status}")
    OrderDto changeOrderStatus(@PathVariable int orderId, @PathVariable OrderStatus status);
}