package net.breezewere.dto;

import java.util.List;

import lombok.Data;

/**
 * OrderUserViewDto is used for List the User All Orders(by user id).
 */
@Data
public class OrderViewDto {

    private long orderId;

    private String status;

    private List<ViewUserOrderFoodItemList> foodItem;

    private long amount;

    private AddressDto address;
}
