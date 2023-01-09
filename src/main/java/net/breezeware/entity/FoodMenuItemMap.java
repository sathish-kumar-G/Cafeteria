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
 * Food Menu Item Map Entity Class, This class Map the two entity(FoodMenu,FoodItem).
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "food_menu_item_map", schema = "cafeteria_management_system")
public class FoodMenuItemMap implements Serializable {

    /**
     * Food Menu Item Map id, Primary key and Unique.
     */
    @Schema(example = "1", description = "Food Menu Item Map Id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "cafeteria_management_system.food_menu_item_map_seq_gen")
    @SequenceGenerator(name = "cafeteria_management_system.food_menu_item_map_seq_gen",
            sequenceName = "cafeteria_management_system.food_menu_item_map_seq", schema = "cafeteria_management_system",
            allocationSize = 1)
    private long foodMenuItemMapId;

    /**
     * Food Menu id, Foreign key and unique.
     */
    @Schema(example = "1", description = "Food Menu Id")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "food_menu_id")
    private FoodMenu foodMenu;

    /**
     * Food Item id, Foreign key and Unique.
     */
    @Schema(example = "1", description = "Food Item Id")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "food_item_id")
    private FoodItem foodItem;

    /**
     * Food Item Count.
     */
    @Schema(example = "10", description = "Food Item Count")
    @Column(name = "food_count")
    private long foodCount;

}
