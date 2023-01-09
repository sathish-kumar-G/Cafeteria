package net.breezeware.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

import lombok.Data;

/**
 * OrderDto is used for User Add Food Items and View the Order Details in the Order.
 */
@Data
public class OrderDto {

    /**
     * Order Food Item details.
     */
    @Schema(example = "Food Items", description = "Order Food Item details")
    private List<OrderFoodItems> orderFoodItems;

}
