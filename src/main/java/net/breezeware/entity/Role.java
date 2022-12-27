package net.breezeware.entity;

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
 * Entity Class for Role of Users.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "role", schema = "cafeteria_management_system")
public class Role {

    /**
     * Role Id, It is Primary Key
     */
    @Schema(example = "1", description = "Role Id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cafeteria_management_system.role_seq_gen")
    @SequenceGenerator(name = "cafeteria_management_system.role_seq_gen",
            sequenceName = "cafeteria_management_system.role_seq", schema = "cafeteria_management_system",
            allocationSize = 1)
    private long roleId;

    /**
     * Role Name of the user
     */
    @Schema(example = "customer", description = "Role Name")
    @Column(name = "role_name")
    private String roleName;

    /**
     * Role Description of the User
     */
    @Schema(example = "user role", description = "Role Description")
    @Column(name = "role_desc")
    private String roleDescription;
}
