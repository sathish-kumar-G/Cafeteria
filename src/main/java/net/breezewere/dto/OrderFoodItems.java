package net.breezewere.dto;

import lombok.Data;

/**
 * OrderFoodItems is used for user add the Food item with Quantity in
 * Order(OrderDto)
 */
@Data
public class OrderFoodItems {

    private long foodItemId;

    private long qty;
}
