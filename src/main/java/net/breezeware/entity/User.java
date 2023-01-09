package net.breezeware.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User Entity Class. Declare all User entity in this Class.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "user", schema = "cafeteria_management_system")
public class User implements Serializable {

    /**
     * user_id  is primary key for this entity.
     */
    @Schema(example = "1", description = "User Id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cafeteria_management_system.user_seq_gen")
    @SequenceGenerator(name = "cafeteria_management_system.user_seq_gen",
            sequenceName = "cafeteria_management_system.user_seq", schema = "cafeteria_management_system",
            allocationSize = 1)
    private long userId;

    /**
     * first_Name for user
     */
    @Schema(example = "sathish", description = "First Name")
    @Column(name = "first_name")
    private String firstName;

    /**
     * last_Name for user
     */
    @Schema(example = "kumar", description = "Last Name")
    @Column(name = "last_name")
    private String lastName;

    /**
     * email_Id for user
     */
    @Schema(example = "sathish@gmail.com", description = "Email Id")
    @Column(name = "email")
    private String emailId;

    /**
     * password for user
     */
    @Schema(example = "sathish@123", description = "Password")
    @Column(name = "password")
    private String password;

    /**
     * role_id Of the User.
     * It is Transient variable.
     */
    @Schema(example = "1", description = "Role Id")
    @Transient
    private long roleId;

}
