package net.breezeware.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OrderAddressMap Entity is used for Add Address for the Order. Foreign key is Order.
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_address_map", schema = "cafeteria_management_system")
public class OrderAddressMap implements Serializable {

    /**
     * Order Address Map id, Primary Key.
     */
    @Schema(example = "1", description = "Order Address Map Id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "cafeteria_management_system.order_address_map_seq_gen")
    @SequenceGenerator(name = "cafeteria_management_system.order_address_map_seq_gen",
            sequenceName = "cafeteria_management_system.order_address_map_seq", schema = "cafeteria_management_system",
            allocationSize = 1)
    private long orderAddressMapId;

    /**
     * Order, Foreign Key.
     */
    @Schema(example = "1", description = "Order Id")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    /**
     * Door Number.
     */
    @Schema(example = "1A", description = "Address Door Number")
    @Column(name = "address_door_no")
    private String doorNo;

    /**
     * Street.
     */
    @Schema(example = "Gandhi Street", description = "Address Street")
    @Column(name = "address_street")
    private String street;

    /**
     * City.
     */
    @Schema(example = "Coimbatore", description = "Address City")
    @Column(name = "address_district")
    private String city;

    /**
     * State.
     */
    @Schema(example = "Tamilnadu", description = "Address State")
    @Column(name = "address_state")
    private String state;

    /**
     * Phone Number.
     */
    @Schema(example = "9524943027", description = "Phone Number")
    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * Ordered Date.
     */
    @Schema(example = "21/12/2022", description = "Order Date")
    @Column(name = "order_date")
    private Date orderDate;
}
