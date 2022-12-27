package net.breezeware.dto;

import java.util.List;

import lombok.Data;

/**
 * OrderUserViewDto is used for List the User All Orders(by user id).
 */
@Data
public class OrderViewDto {

    private long orderId;

    private String status;

    private List<ViewUserOrderFoodItem> foodItem;

    private long amount;

    private AddressDto address;

    public OrderViewDto(long orderId, String status, List<ViewUserOrderFoodItem> foodItem, long amount,
            AddressDto address) {
        this.orderId = orderId;
        this.status = status;
        this.foodItem = foodItem;
        this.amount = amount;
        this.address = address;
    }

    public OrderViewDto() {
    }
}
