package com.asasan.ordermanagement.app.entity;

import com.asasan.ordermanagement.api.dto.ItemDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity @IdClass(OrderItemEntityId.class)
@Table(name = "order_items")
public class OrderItemEntity implements Serializable {

    @Id
    private int orderId;

    @Id
    private int itemId;

    private String itemName;

    private int amount;

    private long price;

    public OrderItemEntity(ItemDto itemDto, int orderId){
        Objects.requireNonNull(itemDto);
        this.itemId = itemDto.getId();
        this.orderId = orderId;
        this.itemName = itemDto.getName();
        this.amount = itemDto.getAmount();
        this.price = itemDto.getPrice();
    }

    public ItemDto toItemDto() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(itemId);
        itemDto.setName(itemName);
        itemDto.setAmount(amount);
        itemDto.setPrice(price);
        return itemDto;
    }


}
