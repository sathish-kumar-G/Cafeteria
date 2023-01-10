package net.breezeware.dto;

import lombok.Data;

/**
 * OrderFoodItems is used for user add the Food item with Quantity in
 * Order(OrderDto).
 */
@Data
public class OrderFoodItems {

    /**
     *Food Item id.
     */
    private long foodItemId;

    /**
     *Food Item Quantity.
     */
    private long qty;
}
