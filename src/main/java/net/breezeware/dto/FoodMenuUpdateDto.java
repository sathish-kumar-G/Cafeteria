package net.breezeware.dto;

import java.util.List;

import lombok.Data;

/**
 * FoodMenuUpdateDto is used for Update the Food Items in the Food Menu
 */
@Data
public class FoodMenuUpdateDto {

    private List<FoodItems> foodItem;

}
