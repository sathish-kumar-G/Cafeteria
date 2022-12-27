package net.breezeware.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.breezeware.entity.Order;
import net.breezeware.entity.OrderAddressMap;

@Repository
public interface OrderAddressMapRepository extends JpaRepository<OrderAddressMap, Long> {

    Optional<OrderAddressMap> findByOrder(Order order);

}
