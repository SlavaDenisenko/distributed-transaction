package com.denisenko.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryEvent {
    private Long orderId;
    private List<OrderLineItem> orderLineItems;
}
