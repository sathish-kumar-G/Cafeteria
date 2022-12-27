package net.breezeware.dto;

import java.util.List;

import net.breezeware.entity.FoodItem;
import net.breezeware.entity.FoodMenu;

import lombok.Data;

/**
 * FoodMenuViewUserDto is used to Get the List of Active FoodMenu by User
 */
@Data
public class FoodMenuViewUserDto {

    private FoodMenu foodMenu;

    private List<FoodItem> foodItems;

}
