package net.breezeware.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

import net.breezeware.entity.FoodItem;
import net.breezeware.entity.FoodMenu;

import lombok.Data;

/**
 * FoodMenuViewUserDto is used to Get the List of Active FoodMenu with Food Items by User
 */
@Data
public class FoodMenuViewUserDto {

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
