package com.asasan.ordermanagement.app.repository;

import com.asasan.ordermanagement.app.entity.OrderEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, Integer> {
}
