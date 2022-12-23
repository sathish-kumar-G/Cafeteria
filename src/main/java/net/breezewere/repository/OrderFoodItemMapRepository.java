package net.breezewere.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.breezewere.entity.FoodItem;
import net.breezewere.entity.Order;
import net.breezewere.entity.OrderFoodItemMap;

@Repository
public interface OrderFoodItemMapRepository extends JpaRepository<OrderFoodItemMap, Long> {

    List<OrderFoodItemMap> findByOrder(Order order);

    Optional<OrderFoodItemMap> findByOrderAndFoodItemFoodItemId(Order order, long foodItemId);

    Optional<OrderFoodItemMap> deleteByOrderAndFoodItem(Order order, FoodItem foodItem);

}
