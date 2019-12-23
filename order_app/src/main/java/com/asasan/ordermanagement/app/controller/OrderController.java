package com.asasan.ordermanagement.app.controller;

import com.asasan.ordermanagement.api.dto.ItemAdditionParametersDto;
import com.asasan.ordermanagement.api.dto.OrderDto;
import com.asasan.ordermanagement.api.dto.OrderStatus;
import com.asasan.ordermanagement.app.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/orders")
public class OrderController {
    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "{orderId}")
    ResponseEntity<OrderDto> getOrderById(@PathVariable int orderId) {
        OrderDto orderById = orderService.getOrderById(orderId);
        if (orderById == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderById);
    }

    @GetMapping
    List<OrderDto> getOrders() { return orderService.getOrders(); }

    @PutMapping(value = "{orderId}/status/{status}")
    ResponseEntity<OrderDto> changeOrderStatus(@PathVariable int orderId, @PathVariable OrderStatus status){
        OrderDto orderDto = orderService.changeOrderStatus(orderId, status);
        if (orderDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderDto);
    }

    @PostMapping(value = "{orderId}/item")
    ResponseEntity<OrderDto> addItemToOrder(@PathVariable Integer orderId,
                                            @RequestBody ItemAdditionParametersDto parametersDto){
        OrderDto orderDto = orderService.addItemToOrder(orderId, parametersDto);
        if (orderDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderDto);
    }

    @PostMapping(value = "/item")
    ResponseEntity<OrderDto> addItemToOrder(@RequestBody ItemAdditionParametersDto parametersDto){
        OrderDto orderDto = orderService.addItemToOrder(null, parametersDto);
        if (orderDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderDto);
    }

}