package net.breezewere.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.breezewere.entity.Order;
import net.breezewere.entity.OrderAddressMap;

@Repository
public interface OrderAddressMapRepository extends JpaRepository<OrderAddressMap, Long> {

    Optional<OrderAddressMap> findByOrder(Order order);

}
