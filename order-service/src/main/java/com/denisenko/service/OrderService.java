package com.denisenko.service;

import com.denisenko.dto.OrderDto;
import com.denisenko.dto.OrderLineItemDto;
import com.denisenko.model.Order;
import com.denisenko.model.OrderLineItem;
import com.denisenko.model.OrderStatus;
import com.denisenko.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import static com.denisenko.model.OrderStatus.PENDING;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;

    public OrderDto createOrder(OrderDto orderDto) {
        Order order = mapToEntity(orderDto);
        order.setStatus(PENDING);
        orderRepository.save(order);
        orderDto.setId(order.getId());
        orderDto.setStatus(order.getStatus().name());
        eventPublisher.publishEvent(orderDto);
        return orderDto;
    }

    public OrderDto getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        return mapToDto(order);
    }

    public void updateOrderStatus(Long orderId, OrderStatus status) {
        orderRepository.updateOrderStatusById(orderId, status);
    }

    private Order mapToEntity(OrderDto orderDto) {
        return Order.builder()
                .orderLineItems(orderDto.getOrderLineItems().stream().map(this::mapToEntity).toList())
                .build();
    }

    private OrderDto mapToDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .status(order.getStatus().name())
                .orderLineItems(order.getOrderLineItems().stream().map(this::mapToDto).toList())
                .build();
    }

    private OrderLineItem mapToEntity(OrderLineItemDto orderLineItemDto) {
        return OrderLineItem.builder()
                .itemId(orderLineItemDto.getItemId())
                .quantity(orderLineItemDto.getQuantity())
                .build();
    }

    private OrderLineItemDto mapToDto(OrderLineItem orderLineItem) {
        return OrderLineItemDto.builder()
                .itemId(orderLineItem.getItemId())
                .quantity(orderLineItem.getQuantity())
                .build();
    }
}
