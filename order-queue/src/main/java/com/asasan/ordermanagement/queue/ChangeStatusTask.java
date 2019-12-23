package com.asasan.ordermanagement.queue;

import com.asasan.ordermanagement.api.dto.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ChangeStatusTask {
    private int orderId;

    private OrderStatus orderStatus;
}
