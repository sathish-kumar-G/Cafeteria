package net.breezeware.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import net.breezeware.dynamo.utils.exception.DynamoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import net.breezeware.dto.FoodItems;
import net.breezeware.dto.FoodMenuDto;
import net.breezeware.dto.FoodMenuUpdateDto;
import net.breezeware.dto.FoodMenuViewDto;
import net.breezeware.dto.FoodMenuViewUserDto;
import net.breezeware.entity.FoodItem;
import net.breezeware.entity.FoodMenu;
import net.breezeware.entity.FoodMenuItemMap;
import net.breezeware.entity.User;
import net.breezeware.entity.UserRoleMap;
import net.breezeware.exception.CustomException;
import net.breezeware.repository.FoodItemRepository;
import net.breezeware.repository.FoodMenuItemMapRepository;
import net.breezeware.repository.FoodMenuRepository;
import net.breezeware.repository.UserRepository;
import net.breezeware.repository.UserRoleMapRepository;

/**
 * FoodMenuServiceImpl class is used to write the Business logic.
 */
@Service
public class FoodMenuServiceImpl implements FoodMenuService {

    @Autowired
    private FoodMenuRepository foodMenuRepository;

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private FoodMenuItemMapRepository foodMenuItemMapRepository;

    @Autowired
    private UserRoleMapRepository userRoleMapRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public FoodMenu createFoodMenu(@Valid FoodMenuDto foodMenuDto, long userId) throws DynamoException {

        // Admin Access Checked using Private Method
        adminAccessChecking(userId);

        // Food Menu Name is Already Available or not Condition Checking
        Optional<FoodMenu> findFoodMenu =
                foodMenuRepository.findByFoodMenuName(foodMenuDto.getFoodMenu().getFoodMenuName());
        if (findFoodMenu.isPresent()) {
            throw new DynamoException("Food Menu is Already Available", HttpStatus.CONFLICT);
        }

        // Empty value Checking Condition
        if (foodMenuDto.getFoodMenu().getFoodMenuName().isEmpty()
                || foodMenuDto.getFoodMenu().getFoodMenuName().isBlank()) {
            throw new DynamoException("Please Enter the Food Menu name", HttpStatus.NOT_FOUND);
        }

        if (foodMenuDto.getFoodMenu().getFoodMenuType().isEmpty()
                || foodMenuDto.getFoodMenu().getFoodMenuType().isBlank()) {
            throw new DynamoException("Please Enter the Food Menu name", HttpStatus.NOT_FOUND);
        }

        if (foodMenuDto.getFoodMenu().getStatus().isEmpty() || foodMenuDto.getFoodMenu().getStatus().isBlank()) {
            throw new DynamoException("Please Enter the Food Menu name", HttpStatus.NOT_FOUND);
        }

        // Food Menu Creation
        FoodMenu foodMenu = new FoodMenu();
        foodMenu.setFoodMenuName(foodMenuDto.getFoodMenu().getFoodMenuName());
        foodMenu.setFoodMenuType(foodMenuDto.getFoodMenu().getFoodMenuType());
        foodMenu.setStatus(foodMenuDto.getFoodMenu().getStatus());

        // Food Items Stored in db
        for (FoodItems foodItem : foodMenuDto.getFoodItems()) {

            // food id check.
            FoodItem retrievedFoodItem = foodItemRepository.findById(foodItem.getFoodItemId())
                    .orElseThrow(() -> new DynamoException("Food Item Is Not Available", HttpStatus.NOT_FOUND));

            // Get the All Food Item in Map table
            Optional<FoodMenuItemMap> foodMenuItemMap = foodMenuItemMapRepository.findByFoodItem(retrievedFoodItem);
            if (foodMenuItemMap.isPresent()) {
                throw new DynamoException("Food Item is Already added in another Food Menu",
                        HttpStatus.ALREADY_REPORTED);
            }

            if (retrievedFoodItem != null) {

                // save Food Menu, Food Item and Count in Map Table
                saveFoodMenuItemMap(foodMenu, retrievedFoodItem, foodItem.getFoodItemCount());

            }

        }

        // Save Food Menu
        return foodMenuRepository.save(foodMenu);

    }

    // Save Food Menu Item Map Table data
    private FoodMenuItemMap saveFoodMenuItemMap(FoodMenu foodMenu, FoodItem foodItem, long count) {

        FoodMenuItemMap foodMenuItemMap = new FoodMenuItemMap();
        foodMenuItemMap.setFoodMenu(foodMenu);
        foodMenuItemMap.setFoodItem(foodItem);
        foodMenuItemMap.setFoodCount(count);
        return foodMenuItemMapRepository.save(foodMenuItemMap);

    }

    // Admin Access Checking Private Method
    private void adminAccessChecking(long userId) throws DynamoException {
        // Admin Access Checking Condition

        // User is Available or Not Condition Checking
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DynamoException("User Is not Available", HttpStatus.NOT_FOUND));

        UserRoleMap userRoleMap = userRoleMapRepository.findByUserId(user)
                .orElseThrow(() -> new DynamoException("Admin Access Only", HttpStatus.NOT_FOUND));

        // Admin Access Checking
        if (userRoleMap.getRoleId().getRoleId() != 1) {
            throw new DynamoException("Not Access For This User", HttpStatus.UNAUTHORIZED);
        }

    }

    // Customer Access Checking Private Method
    private void customerAccessChecking(long userId) throws DynamoException {

        // User is Available or Not Condition Checking
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DynamoException("User Is not Available", HttpStatus.NOT_FOUND));

        UserRoleMap userRoleMap = userRoleMapRepository.findByUserId(user)
                .orElseThrow(() -> new DynamoException("Admin Access Only", HttpStatus.NOT_FOUND));

        // Customer Access Checking
        if (userRoleMap.getRoleId().getRoleId() != 3) {
            throw new DynamoException("Not Access For This User", HttpStatus.UNAUTHORIZED);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FoodMenuViewDto getFoodMenuById(long foodMenuId, long userId) throws DynamoException {

        // Admin Access Checked using Private Method
        adminAccessChecking(userId);

        // Food Menu is Available or not Condition Checking
        FoodMenu foodMenu = foodMenuRepository.findById(foodMenuId)
                .orElseThrow(() -> new DynamoException("Food Menu is Not Available", HttpStatus.NOT_FOUND));

        // Create object for view Food Menu with Food Items
        FoodMenuViewDto foodMenuViewDto = new FoodMenuViewDto();

        // Get the list Of Food Items
        List<FoodMenuItemMap> foodMenuItemMapList = foodMenuItemMapRepository.findByFoodMenu(foodMenu);

        // Get The List Of Food Items
        List<FoodItem> list = new ArrayList<>();

        // For each loop for add Food Item to the List
        for (FoodMenuItemMap foodMenuItem : foodMenuItemMapList) {
            list.add(foodMenuItem.getFoodItem());

        }

        // set the Food Menu
        foodMenuViewDto.setFoodMenu(foodMenu);
        // Set the Food Items
        foodMenuViewDto.setFoodItems(list);
        // Return the Food Menu with Food Items

        return foodMenuViewDto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FoodMenuUpdateDto updateFoodMenu(FoodMenuUpdateDto foodMenuUpdateDto, long foodMenuId, long userId)
            throws DynamoException {

        // Admin Access Checked using Private Method
        adminAccessChecking(userId);

        // Food Menu is Available or not Condition Checking
        FoodMenu foodMenu = foodMenuRepository.findById(foodMenuId)
                .orElseThrow(() -> new DynamoException("FoodMenu is Not Available", HttpStatus.NOT_FOUND));

        // For each loop is used for Update the records
        for (FoodItems updateFoodItems : foodMenuUpdateDto.getFoodItem()) {

            // Get Food Menu and Food Items
            FoodMenuItemMap foodMenuItemMapsList = foodMenuItemMapRepository
                    .findByFoodMenuAndFoodItemFoodItemId(foodMenu, updateFoodItems.getFoodItemId())
                    .orElseThrow(() -> new DynamoException("Food Item is not Available in this Food Menu",
                            HttpStatus.NOT_MODIFIED));

            // System.out.println(foodMenuItemMapsList);

            // Get the Food Item for this Food Menu
            FoodItem foodItem = foodItemRepository.findById(foodMenuItemMapsList.getFoodItem().getFoodItemId())
                    .orElseThrow(() -> new DynamoException("Food Item is Not Available", HttpStatus.NOT_FOUND));
            // System.out.println(foodItem);

            // Update the Food Item Price
            foodItem.setFoodPrice(updateFoodItems.getFoodPrice());

            // Update the Food Item Count in Map table
            foodMenuItemMapsList.setFoodCount(updateFoodItems.getFoodItemCount());

            // Save the Food Item
            foodItemRepository.save(foodItem);
            // System.out.println(foodItem);

            // Save the Food Item Count in Map table
            foodMenuItemMapRepository.save(foodMenuItemMapsList);
            // System.out.println(foodMenuItemMapsList);
        }

        // Return the Updated Food Menu
        return foodMenuUpdateDto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteFoodMenuById(long foodMenuId, long userId) throws DynamoException {

        // Admin Access Checked using Private Method
        adminAccessChecking(userId);

        // Check Food Menu Available or not
        FoodMenu findFoodMenu = foodMenuRepository.findById(foodMenuId)
                .orElseThrow(() -> new DynamoException("Food Menu is Not Available", HttpStatus.NOT_FOUND));

        // Get the List of Food Menu
        List<FoodMenuItemMap> findFoodMenuMap = foodMenuItemMapRepository.findByFoodMenu(findFoodMenu);

        // Check Whether the Food Menu is Empty or not
        if (findFoodMenuMap.isEmpty() || findFoodMenuMap.size() <= 0) {
            throw new DynamoException("Food Menu is Not Available", HttpStatus.NOT_FOUND);
        }

        // Inactive the Food Menus
        for (FoodMenuItemMap delete : findFoodMenuMap) {
            if (delete.getFoodCount() == 0 && delete.getFoodMenu().getStatus() == "inactive") {
                throw new DynamoException("Food Menu is Already Deleted", HttpStatus.ALREADY_REPORTED);
            }

            // Change the Food Menu Status
            delete.getFoodMenu().setStatus("inactive");
            // Change the Food Item Count
            delete.setFoodCount(0);
            // Saved Changes
            foodMenuItemMapRepository.save(delete);
        }

        // foodMenuItemMapRepository.deleteById(findFoodMenuMap);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FoodMenuViewUserDto> viewAllActiveFoodMenuByUser(long userId) throws DynamoException {

        // User Access Checked using Private Method
        customerAccessChecking(userId);

        // Get All Food Menu
        List<FoodMenu> allFoodMenu = foodMenuRepository.findAll();
        // List for Store All Food Menu with Food Items
        List<FoodMenuViewUserDto> foodMenuViewUserDto = new ArrayList<>();
        // For Each loop used for store food menu and food Items value
        for (FoodMenu foodMenu : allFoodMenu) {

            // Create object for dto
            FoodMenuViewUserDto viewUserDto = new FoodMenuViewUserDto();

            // Check the Food Menu is Active or not
            if (foodMenu.getStatus().contentEquals("active")) {
                // save the Food Menu
                viewUserDto.setFoodMenu(foodMenu);
                // List for Store All Food Items for this menu
                List<FoodItem> foodItems = new ArrayList<>();
                // Get the List Of Food Items in the Food Menu
                List<FoodMenuItemMap> maps = foodMenuItemMapRepository.findByFoodMenu(foodMenu);

                // For each loop is used for store each food Items to the list of Food Items
                for (FoodMenuItemMap foodItem : maps) {

                    // Add the Food Item in the list
                    foodItems.add(foodItem.getFoodItem());

                }

                // save the Food Items
                viewUserDto.setFoodItems(foodItems);
                // Add Food Menu and Food Items in the Dto List
                foodMenuViewUserDto.add(viewUserDto);
            }

        }

        // return the Food Menu with Food Items
        return foodMenuViewUserDto;
    }
}
