package net.breezewere.service;

import java.util.List;

import javax.validation.Valid;

import net.breezewere.dto.FoodMenuDto;
import net.breezewere.dto.FoodMenuUpdateDto;
import net.breezewere.dto.FoodMenuViewDto;
import net.breezewere.dto.FoodMenuViewUserDto;
import net.breezewere.entity.FoodMenu;
import net.breezewere.exception.CustomException;

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
