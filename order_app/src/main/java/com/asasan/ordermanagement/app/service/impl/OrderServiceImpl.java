package com.asasan.ordermanagement.app.service.impl;

import com.asasan.ordermanagement.api.dto.ItemAdditionParametersDto;
import com.asasan.ordermanagement.api.dto.ItemDto;
import com.asasan.ordermanagement.api.dto.OrderDto;
import com.asasan.ordermanagement.api.dto.OrderStatus;
import com.asasan.ordermanagement.app.dto.CreateOrderDto;
import com.asasan.ordermanagement.app.entity.OrderEntity;
import com.asasan.ordermanagement.app.entity.OrderItemEntity;
import com.asasan.ordermanagement.app.repository.OrderItemRepository;
import com.asasan.ordermanagement.app.repository.OrderRepository;
import com.asasan.ordermanagement.app.service.OrderService;
import com.asasan.ordermanagement.app.util.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private OrderRepository orderRepository;

    private OrderItemRepository orderItemRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public List<OrderDto> getOrders() {
        return Streams
                .of(orderRepository.findAll())
                .map(this::getOrderDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto getOrderById(int orderId) {
        logger.info("looking for an order by id {}", orderId);
        OrderEntity foundOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("There is no such order in database"));
        return getOrderDto(foundOrder);
    }

    @Override
    public OrderDto changeOrderStatus(int orderId, OrderStatus status) {
        OrderEntity order =  orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("There is no such order in database"));
        order.setStatus(status);
        orderRepository.save(order);

        return order.toOrderDto();
    }

    @Override
    public OrderDto addItemToOrder(Integer orderId, ItemAdditionParametersDto parametersDto) {

        ItemDto item = new ItemDto();
        //check if item with this id exists
        item.setId(parametersDto.getId());
        item.setAmount(parametersDto.getAmount());
        item.setName("Default item name"); //get name of item with this id
        item.setPrice(2); //get price of item with this id

        if (orderId == null) {
            CreateOrderDto createOrderDto = new CreateOrderDto();
            createOrderDto.setTotalAmount(parametersDto.getAmount());
            createOrderDto.setTotalCost(item.getPrice() * item.getAmount());
            createOrderDto.setUserName(parametersDto.getUsername());
            List<ItemDto> items = new ArrayList<>();
            items.add(item);
            createOrderDto.setItems(items);

            OrderEntity order = new OrderEntity(createOrderDto);
            order = orderRepository.save(order);

            OrderItemEntity orderItem = new OrderItemEntity(item, order.getOrderId());
            orderItemRepository.save(orderItem);

            OrderDto orderDto = order.toOrderDto();
            orderDto.setItems(items);
            return orderDto;

        } else {
            OrderEntity order =  orderRepository.findById(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("There is no such order in database"));

            try {
                //if record with these orderId and itemId exists
                OrderItemEntity orderItem = orderItemRepository.findByOrderIdAndItemId(order.getOrderId(), item.getId());
                orderItem.setAmount(orderItem.getAmount() + item.getAmount());
                orderItemRepository.save(orderItem);
                order.setTotalAmount(order.getTotalAmount() + item.getAmount());
                order.setTotalCost(order.getTotalCost() + item.getAmount() * item.getPrice());
            }catch (Exception e){
                //else create the record
                OrderItemEntity orderItem = new OrderItemEntity(item, order.getOrderId());
                orderItemRepository.save(orderItem);
                order.setTotalAmount(order.getTotalAmount() + item.getAmount());
                order.setTotalCost(order.getTotalCost() + item.getAmount() * item.getPrice());
            }
            orderRepository.save(order);

            return getOrderDto(order);
        }
    }

    private OrderDto getOrderDto(OrderEntity order) {
        List<OrderItemEntity> foundOrderItems = orderItemRepository.findAllByOrderId(order.getOrderId());
        List<ItemDto> orderItems = Streams
                .of(foundOrderItems)
                .map(OrderItemEntity::toItemDto)
                .collect(Collectors.toList());
        OrderDto orderDto = order.toOrderDto();
        orderDto.setItems(orderItems);
        return orderDto;
    }


}
