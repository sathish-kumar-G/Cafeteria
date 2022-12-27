package net.breezeware.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.breezeware.entity.FoodItem;
import net.breezeware.entity.FoodMenu;
import net.breezeware.entity.FoodMenuItemMap;

@Repository
public interface FoodMenuItemMapRepository extends JpaRepository<FoodMenuItemMap, Long> {

    Optional<FoodMenuItemMap> findByFoodItem(FoodItem foodItems);

    List<FoodMenuItemMap> findByFoodMenu(FoodMenu foodMenu);

    // Optional<FoodMenuItemMap> findByFoodItemFoodItemId(long foodMenuItemMapList);

    void deleteByFoodMenu(FoodMenu foodMenuId);

    Optional<FoodMenuItemMap> findByFoodMenuFoodMenuId(long foodMenuId);

    Optional<FoodMenuItemMap> findByFoodMenuAndFoodItemFoodItemId(FoodMenu foodMenu, long foodItemId);

    // FoodMenuItemMap findByFoodMenuFoodMenuName(String foodMenuName);

    // FoodItem findByFoodMenuId(FoodMenu foodMenuId);

    // FoodMenuItemMap findFoodItemIdFoodName(String foodItemName);

    // FoodMenuItemMap findByFoodMenuId(long foodMenuId);

}
