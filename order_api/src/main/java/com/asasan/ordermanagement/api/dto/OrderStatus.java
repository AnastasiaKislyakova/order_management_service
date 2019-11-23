package com.asasan.ordermanagement.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OrderStatus {
    @JsonProperty("Collecting")
    COLLECTING,
    @JsonProperty("Paid")
    PAID,
    @JsonProperty("Shipping")
    SHIPPING,
    @JsonProperty("Complete")
    COMPLETE,
    @JsonProperty("Failed")
    FAILED,
    @JsonProperty("Cancelled")
    CANCELLED
}