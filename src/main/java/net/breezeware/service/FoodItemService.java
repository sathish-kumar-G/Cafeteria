package net.breezeware.service;

import java.util.List;

import javax.validation.Valid;

import net.breezeware.dynamo.utils.exception.DynamoException;
import net.breezeware.entity.FoodItem;
import net.breezeware.exception.CustomException;

/**
 * FoodItemServiceImpl Class implements FoodItemService Interface.
 */
public interface FoodItemService {

    /**
     * Create the new Food Item by Admin.
     * @param  foodItem        this foodItem data is to be created.
     * @param  userId          this id is used to find the Admin.
     * @return                 Created new Food Item.
     * @throws DynamoException if foodItem is null or conflict value.
     */
    FoodItem createFoodItem(@Valid FoodItem foodItem, long userId) throws DynamoException;

    /**
     * Gets all the foodItem by Admin.
     * @param  userId this id is used to find the Admin.
     * @return        All the Food items.
     */
    List<FoodItem> viewAllFoodItem(long userId) throws DynamoException;

    /**
     * Update the Food Item by admin.
     * @param  foodItem        update the foodItem data.
     * @param  foodItemId      this id is used to find the foodItem.
     * @param  userId          this id is used to find the Admin.
     * @return                 the updated Food item.
     * @throws DynamoException if foodItem or Admin is not found.
     */
    FoodItem updateFoodItemById(@Valid FoodItem foodItem, long foodItemId, long userId) throws DynamoException;

    /**
     * Delete the Food Item by Admin
     * @param  foodItemId      this id is used to find the foodItem.
     * @param  userId          this id is used to find the Admin.
     * @throws DynamoException if foodItem or Admin is not found.
     */
    void deleteFoodItemById(long foodItemId, long userId) throws DynamoException;

}
