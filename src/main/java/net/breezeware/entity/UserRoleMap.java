package net.breezeware.entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
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
 * User Role Map Entity Class, This class Map the two entity(User,Role).
 */

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "user_role_map", schema = "cafeteria_management_system")
public class UserRoleMap implements Serializable {

    /**
     * User Role Map id, Primary key and Unique.
     */
    @Schema(example = "1", description = "User Role Map Id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cafeteria_management_system.user_role_map_seq_gen")
    @SequenceGenerator(name = "cafeteria_management_system.user_role_map_seq_gen",
            sequenceName = "cafeteria_management_system.user_role_map_seq", schema = "cafeteria_management_system",
            allocationSize = 1)
    private long userRoleMapId;

    /**
     * User Id, Foreign key and unique.
     */
    @Schema(example = "1", description = "User Id")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User userId;

    /**
     * Role Id, Foreign key and unique.
     */
    @Schema(example = "1", description = "Role Id")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private Role roleId;

}
