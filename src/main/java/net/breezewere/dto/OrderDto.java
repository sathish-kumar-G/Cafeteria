package net.breezewere.dto;

import java.util.List;

import lombok.Data;

/**
 * OrderDto is used for User Add Food Items and View the Order Details in the
 * Order
 */
@Data
public class OrderDto {

    private List<OrderFoodItems> orderFoodItems;

}
