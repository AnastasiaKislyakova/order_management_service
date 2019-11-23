package com.asasan.ordermanagement.app.controller;

import com.asasan.ordermanagement.api.dto.ItemAdditionParametersDto;
import com.asasan.ordermanagement.api.dto.OrderDto;
import com.asasan.ordermanagement.api.dto.OrderStatus;
import com.asasan.ordermanagement.app.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("orders")
public class OrderController {
    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "{orderId}")
    OrderDto getOrderById(@PathVariable int orderId) {
        return orderService.getOrderById(orderId);
    }

    @GetMapping
    List<OrderDto> getOrders() { return orderService.getOrders(); }

    @PutMapping(value = "{orderId}/status/{status}")
    OrderDto changeOrderStatus(@PathVariable int orderId, @PathVariable OrderStatus status){
        return orderService.changeOrderStatus(orderId, status);
    }

    @PostMapping(value = "{orderId}/item")
    OrderDto addItemToOrder(@PathVariable Integer orderId, @RequestBody ItemAdditionParametersDto parametersDto){
        return orderService.addItemToOrder(orderId, parametersDto);
    }

    @PostMapping(value = "/item")
    OrderDto addItemToOrder(@RequestBody ItemAdditionParametersDto parametersDto){
        return orderService.addItemToOrder(null, parametersDto);
    }



//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    OrderDto createUser(@Valid @RequestBody CreateOrderDto createOrderDto) {
//        return orderService.createOrder(createOrderDto);
//    }
}