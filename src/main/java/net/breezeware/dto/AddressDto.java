package net.breezeware.dto;

import java.sql.Date;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * AddressDto is used to represent the Order address of the user.
 */
@Data
public class AddressDto {

    /**
     * Address Door Number.
     */
    @Schema(example = "9A", description = "Door Number")
    private String doorNo;

    /**
     * Address Street Name.
     */
    @Schema(example = "Vijay Street", description = "Street Name")
    private String street;

    /**
     * Address City Name.
     */
    @Schema(example = "Coimbatore", description = "City Name")
    private String city;

    /**
     * Address State Name.
     */
    @Schema(example = "TamilNadu", description = "State Name")
    private String state;

    /**
     * Address Phone Number.
     */
    @Schema(example = "9876798231", description = "Phone Number")
    private String phoneNumber;

    /**
     * Order Date.
     */
    @Schema(example = "10/11/2022", description = "Ordered Date")
    private Date orderDate;
}
