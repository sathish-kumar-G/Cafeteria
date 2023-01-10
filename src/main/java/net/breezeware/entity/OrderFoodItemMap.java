package net.breezeware.entity;

import java.io.Serializable;
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
 * OrderFoodItemMap is represents the Order With Address,
 * Foreign key is Order.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_food_item_map", schema = "cafeteria_management_system")
public class OrderFoodItemMap implements Serializable {

    /**
     * Primary Key Order FoodItem Map id.
     */
    @Schema(example = "1", description = "Order FoodItem Map Id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "cafeteria_management_system.order_food_item_map_seq_gen")
    @SequenceGenerator(name = "cafeteria_management_system.order_food_item_map_seq_gen",
            sequenceName = "cafeteria_management_system.order_food_item_map_seq",
            schema = "cafeteria_management_system", allocationSize = 1)
    private long OrderFoodItemMapId;

    /**
     * Foreign Key Order details.
     */
    @Schema(example = "4", description = "Order Id")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    /**
     * Foreign Key Food Item Data.
     */
    @Schema(example = "4", description = "Food Item Id")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "food_item_id")
    private FoodItem foodItem;

    @Schema(example = "4", description = "Food Item quantity")
    @Column(name = "quantity")
    private long quantity;
}
