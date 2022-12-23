package net.breezewere.entity;

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

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order", schema = "cafeteria_management_system")
public class Order {

    /**
     * Order id, Primary key and unique.
     */
    @Schema(example = "1", description = "Order Id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cafeteria_management_system.order_seq_gen")
    @SequenceGenerator(name = "cafeteria_management_system.order_seq_gen",
            sequenceName = "cafeteria_management_system.order_seq", schema = "cafeteria_management_system",
            allocationSize = 1)
    private long orderId;

    /**
     * Order Status, Status Of the Order.
     */
    @Schema(example = "active", description = "Order Status")
    @Column(name = "order_status")
    private String status;

    @Schema(example = "1", description = "User Details")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Schema(example = "2000", description = "Order Total Amount")
    @Column(name = "order_amount")
    private long amount;
}
