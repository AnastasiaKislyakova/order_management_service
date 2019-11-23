package com.asasan.ordermanagement.app.repository;

import com.asasan.ordermanagement.app.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends CrudRepository<OrderItemEntity, Integer> {

    @Query(value = "select oi from OrderItemEntity oi where oi.orderId = ?1 and oi.itemId = ?2")
    OrderItemEntity findByOrderIdAndItemId(int orderId, int itemId);

    @Query(value = "select oi from OrderItemEntity oi where oi.orderId = ?1")
    List<OrderItemEntity> findAllByOrderId(int orderId);
}
