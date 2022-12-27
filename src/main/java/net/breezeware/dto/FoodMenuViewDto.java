package net.breezeware.dto;

import java.util.List;

import net.breezeware.entity.FoodItem;
import net.breezeware.entity.FoodMenu;

import lombok.Data;

/**
 * FoodMenuViewDto is used for Get the Food Menu with Food Items for Admin
 */
@Data
public class FoodMenuViewDto {

    private FoodMenu foodMenu;

    private List<FoodItem> foodItems;

}
