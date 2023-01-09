package net.breezeware.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OrderUserViewDto is used for List the User All Orders(by user id).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderViewDto {

    /**
     * Order id.
     */
    @Schema(example = "1", description = "Order ID")
    private long orderId;

    /**
     * Order Status.
     */
    @Schema(example = "active", description = "Order Status")
    private String status;

    /**
     * Food Items details.
     */
    @Schema(example = "Food Items", description = "Order Food Item details")
    private List<ViewUserOrderFoodItem> foodItem;

    /**
     * Order Amount.
     */
    @Schema(example = "1000", description = "Order Amount")
    private long amount;

    /**
     * Order Address details.
     */
    @Schema(example = "Addess Details", description = "Order Address details")
    private AddressDto address;

}
