package net.breezeware.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

import net.breezeware.entity.FoodMenu;

import lombok.Data;

/**
 * {@link FoodMenuDto} is used to create the Food Menu with Food Items.
 */
@Data
public class FoodMenuDto {

    /**
     * Food Menu Details.
     */
    @Schema(description = "Food Menu")
    private FoodMenu foodMenu;

    /**
     * List of Food Item Details.
     */
    @Schema(description = "Food Items")
    private List<FoodItems> foodItems;

}
