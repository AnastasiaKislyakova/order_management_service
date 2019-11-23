package com.asasan.ordermanagement.app.dto;

import com.asasan.ordermanagement.api.dto.ItemDto;
import com.asasan.ordermanagement.api.dto.OrderStatus;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
public class CreateOrderDto {

    @NotNull
    private OrderStatus status = OrderStatus.COLLECTING;

    private long totalCost;

    private int totalAmount;

    @NotBlank
    private String userName;

    @NotNull
    private List<ItemDto> items;
}