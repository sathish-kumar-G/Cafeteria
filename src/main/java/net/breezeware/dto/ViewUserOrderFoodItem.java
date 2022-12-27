package net.breezeware.dto;

import lombok.Data;

/**
 * ViewUserOrderFoodItemList is used for FoodItems with
 * Quantity(ViewUserOrderFoodItems ->> OrderUserViewDto and OrderUpdateDto)
 */
@Data
public class ViewUserOrderFoodItem {

    private long foodItemId;

    private String foodName;

    private long foodPrice;

    private long qty;
}
