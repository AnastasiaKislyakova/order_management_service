package com.asasan.ordermanagement.api.dto;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDto {

    private int id;

    private OrderStatus status;

    private long totalCost;

    private int totalAmount;

    private String userName;

    private List<ItemDto> items;
}
