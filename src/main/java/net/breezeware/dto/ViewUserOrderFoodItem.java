package net.breezeware.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * ViewUserOrderFoodItem is used for represents the FoodItems with Quantity.
 */
@Data
public class ViewUserOrderFoodItem {

    /**
     * Food Item id.
     */
    @Schema(example = "1", description = "Food Item ID")
    private long foodItemId;

    /**
     * Food Name.
     */
    @Schema(example = "Pizza", description = "Food Name")
    private String foodName;

    /**
     * Food Price.
     */
    @Schema(example = "125", description = "Food Price")
    private long foodPrice;

    /**
     * Food Item Quality.
     */
    @Schema(example = "13", description = "Food Item Quality")
    private long qty;
}
