package net.breezeware.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

import lombok.Data;

/**
 * OrderUpdateDto is used for Set and Get the Updated Food Items in the Order.
 */
@Data
public class OrderUpdateDto {

    /**
     * Updated Food Items details.
     */
    @Schema(example = "Food Items", description = "Order Food Item details")
    private List<ViewUserOrderFoodItem> foodItem;

    /**
     * Order Amount
     */
    @Schema(example = "2343", description = "Order Food Item Amount")
    private long amount;

}
