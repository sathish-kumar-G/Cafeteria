package net.breezewere.dto;

import java.util.List;

import net.breezewere.entity.FoodItem;
import net.breezewere.entity.FoodMenu;

import lombok.Data;

/**
 * FoodMenuViewDto is used for Get the Food Menu with Food Items for Admin
 */
@Data
public class FoodMenuViewDto {

    private FoodMenu foodMenu;

    private List<FoodItem> foodItems;

}
