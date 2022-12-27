package net.breezeware.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.breezeware.entity.FoodItem;
import net.breezeware.entity.Order;
import net.breezeware.entity.OrderFoodItemMap;

@Repository
public interface OrderFoodItemMapRepository extends JpaRepository<OrderFoodItemMap, Long> {

    List<OrderFoodItemMap> findByOrder(Order order);

    Optional<OrderFoodItemMap> findByOrderAndFoodItemFoodItemId(Order order, long foodItemId);

    Optional<OrderFoodItemMap> deleteByOrderAndFoodItem(Order order, FoodItem foodItem);

    //void deleteByOrder(Order order);


}
