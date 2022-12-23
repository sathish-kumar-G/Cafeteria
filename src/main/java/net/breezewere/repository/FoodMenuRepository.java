package net.breezewere.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.breezewere.entity.FoodMenu;

@Repository
public interface FoodMenuRepository extends JpaRepository<FoodMenu, Long> {

    Optional<FoodMenu> findByFoodMenuName(String foodMenuName);

    Optional<FoodMenu> findByFoodMenuId(long foodMenuId);

    FoodMenu deleteById(long foodMenuId);

    void deleteByFoodMenuId(FoodMenu foodMenuId);

    // void deleteById(FoodMenu foodMenuId);

}
