package net.breezewere.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Food Item Entity Class
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "food_item", schema = "cafeteria_management_system")
public class FoodItem {

    /**
     * Food Item Id, Primary key and Unique.
     */
    @Schema(example = "1", description = "Food Item Id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cafeteria_management_system.food_item_seq_gen")
    @SequenceGenerator(name = "cafeteria_management_system.food_item_seq_gen",
            sequenceName = "cafeteria_management_system.food_item_seq", schema = "cafeteria_management_system",
            allocationSize = 1)
    private long foodItemId;

    /**
     * Food Item Name
     */
    @Schema(example = "pizza", description = "Food Item Name")
    @Column(name = "food_name")
    private String foodName;

    /**
     * Food Item Price
     */
    @Schema(example = "500", description = "Food Item Price")
    @Column(name = "food_price")
    private long foodPrice;

}
