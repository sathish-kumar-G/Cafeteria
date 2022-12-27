package net.breezeware.service;

import java.util.List;

import javax.validation.Valid;

import net.breezeware.entity.FoodItem;
import net.breezeware.exception.CustomException;

public interface FoodItemService {

    /**
     * @param  foodItem        create the new Food Item
     * @param  userId          find Admin
     * @return                 new Food Item
     * @throws CustomException if null and conflict value then throw exception.
     */
    FoodItem createFoodItem(@Valid FoodItem foodItem, long userId) throws CustomException;

    /**
     * @param  userId find Admin
     * @return        All the Food items in the list.
     */
    List<FoodItem> viewAllFoodItem(long userId) throws CustomException;

    /**
     * @param  foodItem        update the food item by using id.
     * @param  foodItemId      By using Id
     * @param  userId          find Admin
     * @return                 the updated Food item
     * @throws CustomException
     */
    FoodItem updateFoodItemById(@Valid FoodItem foodItem, long foodItemId, long userId) throws CustomException;

    /**
     * @param  foodItemId      Delete the Food item by id.
     * @param  userId          find Admin
     * @return                 Delete success message
     * @throws CustomException throws if food item is not available.
     */
    void deleteFoodItemById(long foodItemId, long userId) throws CustomException;

}
