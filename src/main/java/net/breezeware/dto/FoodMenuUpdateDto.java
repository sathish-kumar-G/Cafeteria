package net.breezeware.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

import lombok.Data;

/**
 * FoodMenuUpdateDto is used for Update the Food Items in the respective Food Menu.
 */
@Data
public class FoodMenuUpdateDto {

    /**
     * List of Updated Food Item Details.
     */
    @Schema(description = "Food Items")
    private List<FoodItems> foodItem;

}
