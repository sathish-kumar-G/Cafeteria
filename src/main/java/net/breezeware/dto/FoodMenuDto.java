package net.breezeware.dto;

import java.util.List;

import net.breezeware.entity.FoodMenu;

import lombok.Data;

/**
 * {@link FoodMenuDto} is used for Add the Food Items in Food Menu
 */
@Data
public class FoodMenuDto {

    private FoodMenu foodMenu;

    private List<FoodItems> foodItems;

}
