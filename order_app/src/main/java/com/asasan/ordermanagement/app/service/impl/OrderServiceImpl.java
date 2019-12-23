package com.asasan.ordermanagement.app.service.impl;

import com.asasan.ordermanagement.api.dto.ItemAdditionParametersDto;
import com.asasan.ordermanagement.api.dto.ItemDto;
import com.asasan.ordermanagement.api.dto.OrderDto;
import com.asasan.ordermanagement.api.dto.OrderStatus;
import com.asasan.ordermanagement.api.feign.OrderManagementServiceClient;
import com.asasan.ordermanagement.app.dto.CreateOrderDto;
import com.asasan.ordermanagement.app.entity.OrderEntity;
import com.asasan.ordermanagement.app.entity.OrderItemEntity;
import com.asasan.ordermanagement.app.repository.OrderItemRepository;
import com.asasan.ordermanagement.app.repository.OrderRepository;
import com.asasan.ordermanagement.app.service.OrderService;
import com.asasan.ordermanagement.app.util.Streams;
import com.asasan.warehousemanagement.api.feign.WarehouseManagementServiceClient;
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
    private WarehouseManagementServiceClient warehouseClient;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderItemRepository orderItemRepository,
                            WarehouseManagementServiceClient warehouseClient) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.warehouseClient = warehouseClient;
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
        return orderRepository.findById(orderId)
                .map(this::getOrderDto)
                .orElse(null);
    }

    @Override
    public OrderDto changeOrderStatus(int orderId, OrderStatus status) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElse(null);
        if (order == null) return null;
        order.setStatus(status);

        if (status == OrderStatus.FAILED || status == OrderStatus.CANCELLED) {
            List<OrderItemEntity> foundOrderItems = orderItemRepository.findAllByOrderId(order.getOrderId());
            Streams.of(foundOrderItems)
                    .map((item) -> warehouseClient.changeItemAmount(item.getItemId(), item.getAmount()));
        }
        orderRepository.save(order);
        return order.toOrderDto();
    }

    @Override
    public OrderDto addItemToOrder(Integer orderId, ItemAdditionParametersDto parametersDto) {

        ItemDto item = convertItemDto(warehouseClient.getItemById(parametersDto.getId()));
        if (item == null) {
            return null;
        }

        if (item.getAmount() < parametersDto.getAmount()) {
            return null;
        }

        if (orderId == null) {
            warehouseClient.changeItemAmount(item.getId(), -parametersDto.getAmount());
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
            OrderEntity order = orderRepository.findById(orderId)
                    .orElse(null);
            if (order == null) {
                return null;
            }

            warehouseClient.changeItemAmount(item.getId(), -parametersDto.getAmount());

            OrderItemEntity orderItem = orderItemRepository.findByOrderIdAndItemId(order.getOrderId(), item.getId());
            if (orderItem == null) {
                orderItem = new OrderItemEntity(item, order.getOrderId());
            }
            orderItem.setAmount(orderItem.getAmount() + parametersDto.getAmount());
            orderItemRepository.save(orderItem);
            order.setTotalAmount(order.getTotalAmount() + parametersDto.getAmount());
            order.setTotalCost(order.getTotalCost() + parametersDto.getAmount() * item.getPrice());
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

    private ItemDto convertItemDto(com.asasan.warehousemanagement.api.dto.ItemDto itemDto) {
        if (itemDto == null) return null;
        ItemDto item = new ItemDto();
        item.setId(itemDto.getId());
        item.setPrice(itemDto.getPrice());
        item.setAmount(itemDto.getAmount());
        item.setName(itemDto.getName());
        return item;
    }


}
