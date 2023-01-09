package net.breezeware.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

import net.breezeware.entity.FoodItem;
import net.breezeware.entity.FoodMenu;

import lombok.Data;

/**
 * FoodMenuViewDto is used for Get the Food Menu with Food Items for Admin.
 */
@Data
public class FoodMenuViewDto {

    /**
     * Food Menu Details.
     */
    @Schema(description = "Food Menu")
    private FoodMenu foodMenu;

    /**
     * List of Food Item Details.
     */
    @Schema(description = "Food Items")
    private List<FoodItem> foodItems;

}
