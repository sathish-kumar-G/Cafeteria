package net.breezeware.dto;

import java.util.List;

import lombok.Data;

/**
 * OrderUpdateDto is used for Set and Get the Updated Food Items in Order
 */
@Data
public class OrderUpdateDto {

    private List<ViewUserOrderFoodItem> foodItem;

    private long amount;

}
