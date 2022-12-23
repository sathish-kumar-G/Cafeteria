package net.breezewere.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class AddressDto {

    private String doorNo;

    private String street;

    private String city;

    private String state;

    private String phoneNumber;

    private Date orderDate;
}
