package net.breezewere.entity;

import java.io.Serializable;

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
 * Food Menu Entity Class
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "food_menu", schema = "cafeteria_management_system")
public class FoodMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Food menu id, Primary key and unique.
     */
    @Schema(example = "1", description = "Food Menu Id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cafeteria_management_system.food_menu_seq_gen")
    @SequenceGenerator(name = "cafeteria_management_system.food_menu_seq_gen",
            sequenceName = "cafeteria_management_system.food_menu_seq", schema = "cafeteria_management_system",
            allocationSize = 1)
    private long foodMenuId;

    /**
     * Food Menu Name
     */
    @Schema(example = "Indian", description = "Food Menu Name")
    @Column(name = "food_menu_name")
    private String foodMenuName;

    /**
     * Food Menu Type
     */
    @Schema(example = "veg", description = "Food Menu Type")
    @Column(name = "food_menu_type")
    private String foodMenuType;

    /**
     * Food Menu Active or NotActive
     */
    @Schema(example = "active", description = "Food Menu is Active or not")
    @Column(name = "food_menu_available_date")
    private String status;

}
