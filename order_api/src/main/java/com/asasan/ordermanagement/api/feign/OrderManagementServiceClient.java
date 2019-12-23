package com.asasan.ordermanagement.api.feign;

import com.asasan.ordermanagement.api.dto.ItemAdditionParametersDto;
import com.asasan.ordermanagement.api.dto.OrderDto;
import com.asasan.ordermanagement.api.dto.OrderStatus;
import com.asasan.ordermanagement.api.service.OrderManagementServiceApi;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

public interface OrderManagementServiceClient extends OrderManagementServiceApi {

    @RequestLine("GET /")
    List<OrderDto> getOrders();

    @RequestLine("GET /{orderId}")
    OrderDto getOrderById(@Param("orderId") int orderId);

    @RequestLine("GET /{orderId}/item")
    OrderDto addItemToOrder(@Param("orderId") Integer orderId, ItemAdditionParametersDto parametersDto);

    @RequestLine("GET /item")
    OrderDto addItemToOrder(ItemAdditionParametersDto parametersDto);

    @RequestLine("GET /{orderId}/status/{status}")
    OrderDto changeOrderStatus(@Param("orderId") int orderId, @Param("status") OrderStatus status);
}