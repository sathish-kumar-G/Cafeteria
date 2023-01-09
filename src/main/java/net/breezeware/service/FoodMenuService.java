package net.breezeware.service;

import java.util.List;

import javax.validation.Valid;

import net.breezeware.dto.FoodMenuDto;
import net.breezeware.dto.FoodMenuUpdateDto;
import net.breezeware.dto.FoodMenuViewDto;
import net.breezeware.dto.FoodMenuViewUserDto;
import net.breezeware.entity.FoodMenu;
import net.breezeware.exception.CustomException;

/**
 * FoodMenuServiceImpl Class implements FoodMenuService Interface.
 */
public interface FoodMenuService {

    /**
     * Create the new Food Menu with Food Items by Admin.
     * @param  foodMenuDto     Food Menu with Food Items details.
     * @param  userId          this id is used to find the admin.
     * @return                 Created new Food menu with Food Items.
     * @throws CustomException if food menu details null or conflict.
     */
    FoodMenu createFoodMenu(@Valid FoodMenuDto foodMenuDto, long userId) throws CustomException;

    /**
     * Gets the Food Menu with Food items by Admin.
     * @param  foodMenuId      this id is used to fins the foodMenu
     * @param  userId          this id is used to find the admin.
     * @return                 the Food Menu with Food Items
     * @throws CustomException if foodMenu or Admin is not found.
     */
    FoodMenuViewDto getFoodMenuById(long foodMenuId, long userId) throws CustomException;

    /**
     * Update the Food Menu with Food Items by Admin.
     * @param  foodMenuUpdateDto Update the Food Menu and Food Items data.
     * @param  foodMenuId        this id is used to fins the foodMenu.
     * @param  userId            this id is used to find the admin.
     * @return                   Updated Food menu with Food Items.
     * @throws CustomException   if foodMenu or Admin is not found.
     */
    FoodMenuUpdateDto updateFoodMenu(FoodMenuUpdateDto foodMenuUpdateDto, long foodMenuId, long userId)
            throws CustomException;

    /**
     * Delete the Food Menu with Food Items by Admin.
     * @param  foodMenuId      this id is used to fins the foodMenu.
     * @param  userId          this id is used to find the admin.
     * @throws CustomException if foodMenu or Admin is not found.
     */
    void deleteFoodMenuById(long foodMenuId, long userId) throws CustomException;

    /**
     * Gets the all Active Food Menu with Food Items by User.
     * @param  userId          this id is used to find the user.
     * @return                 All the Active Food Menu with Food Items.
     * @throws CustomException if foodMenu or User is not found.
     */
    List<FoodMenuViewUserDto> viewAllActiveFoodMenuByUser(long userId) throws CustomException;

}
