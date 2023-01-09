package net.breezeware.dto;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * FoodItems is used to represent the Food Item. This dto is used to represent
 * the Food Item with price and count.
 */
@Data
public class FoodItems {

    /**
     * Food Item id
     */
    @Schema(example = "1", description = "Food Item Id")
    private long foodItemId;

    /**
     * Food Item Price
     */
    @Schema(example = "144", description = "Food Item Price")
    private long foodPrice;

    /**
     * Food Item Count
     */
    @Schema(example = "12", description = "Food Item Count")
    private long foodItemCount;
}
