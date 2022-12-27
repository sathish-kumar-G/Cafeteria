package net.breezeware.service;

import java.util.List;

import javax.validation.Valid;

import net.breezeware.dto.FoodMenuDto;
import net.breezeware.dto.FoodMenuUpdateDto;
import net.breezeware.dto.FoodMenuViewDto;
import net.breezeware.dto.FoodMenuViewUserDto;
import net.breezeware.entity.FoodMenu;
import net.breezeware.exception.CustomException;

public interface FoodMenuService {

    /**
     * @param  foodMenuDto     Create the Food Menu
     * @param  userId          find Admin
     * @return                 save the Food menu in DB.
     * @throws CustomException throw exception if already created or empty value
     *                         present.
     */
    FoodMenu createFoodMenu(@Valid FoodMenuDto foodMenuDto, long userId) throws CustomException;

    /**
     * @param foodMenuId Delete The Food Menu for thid Id.
     * @param userId     find Admin
     */
    void deleteFoodMenuById(long foodMenuId, long userId) throws CustomException;

    /**
     * @param  foodMenuId      Find The Food Menu By Id
     * @param  userId          find Admin
     * @return                 the Food Menu with Food Items
     * @throws CustomException throw exception.
     */
    FoodMenuViewDto getFoodMenuById(long foodMenuId, long userId) throws CustomException;

    /**
     * @param  foodMenuUpdateDto Update the Food Menu and Food Items.
     * @param  foodMenuId        Update the Food Menu using id.
     * @param  userId            find Admin
     * @return
     * @throws CustomException   throw exception.
     */
    FoodMenuUpdateDto updateFoodMenu(FoodMenuUpdateDto foodMenuUpdateDto, long foodMenuId, long userId)
            throws CustomException;

    /**
     * @param  userId          find Admin
     * @return
     * @throws CustomException throw exception.
     */
    List<FoodMenuViewUserDto> getAllActiveFoodMenu(long userId) throws CustomException;

}
