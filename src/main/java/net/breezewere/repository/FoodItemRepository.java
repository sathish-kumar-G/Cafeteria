package net.breezewere.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.breezewere.entity.FoodItem;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {

    Optional<FoodItem> findByFoodName(String foodName);

    void deleteById(long foodItemId);

    void deleteByFoodItemId(FoodItem foodItemId);

}
