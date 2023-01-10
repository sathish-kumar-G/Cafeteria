package net.breezeware.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import net.breezeware.dynamo.utils.exception.DynamoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import net.breezeware.entity.FoodItem;
import net.breezeware.entity.User;
import net.breezeware.entity.UserRoleMap;
import net.breezeware.exception.CustomException;
import net.breezeware.repository.FoodItemRepository;
import net.breezeware.repository.UserRepository;
import net.breezeware.repository.UserRoleMapRepository;

/**
 * FoodItemServiceImpl class is used to write the Business logic.
 */
@Service
public class FoodItemServiceImpl implements FoodItemService {

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleMapRepository userRoleMapRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public FoodItem createFoodItem(@Valid FoodItem foodItem, long userId) throws DynamoException {

        // Admin Access Checking using Private Method
        adminAccessChecking(userId);

        // Already Food Item created or not Condition Checking
        Optional<FoodItem> existItem = foodItemRepository.findByFoodName(foodItem.getFoodName());

        if (existItem.isPresent()) {
            throw new DynamoException("This Food Item Is Already Added", HttpStatus.CONFLICT);
        }

        // Empty Value Condition Checking
        if (foodItem.getFoodName().isEmpty() || foodItem.getFoodName().isBlank()) {
            throw new DynamoException("Please Fill the Food Name", HttpStatus.BAD_REQUEST);
        }

        if (java.util.Objects.isNull(foodItem.getFoodPrice())) {
            throw new DynamoException("Please Fill the Food price", HttpStatus.BAD_REQUEST);
        }

        // Save Food Item
        return foodItemRepository.save(foodItem);
    }

    // Private method is used for Check Admin Access process
    private void adminAccessChecking(long userId) throws DynamoException {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FoodItem> viewAllFoodItem(long userId) throws DynamoException {

        // Admin Access Checking using Private Method
        adminAccessChecking(userId);

        // View All Food Items
        return foodItemRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FoodItem updateFoodItemById(@Valid FoodItem foodItem, long foodItemId, long userId) throws DynamoException {

        // Admin Access Checking using Private Method
        adminAccessChecking(userId);

        // Food Item is Available or not Condition Checking
        FoodItem updateFoodItem = foodItemRepository.findById(foodItemId)
                .orElseThrow(() -> new DynamoException("Food Item is not Available", HttpStatus.NOT_FOUND));

        // Update the New Records
        // Set the Updated value
        updateFoodItem.setFoodName(foodItem.getFoodName());
        updateFoodItem.setFoodPrice(foodItem.getFoodPrice());

        // Save the Updated Records
        return foodItemRepository.save(updateFoodItem);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteFoodItemById(long foodItemId, long userId) throws DynamoException {

        // Admin Access Checking using Private Method
        adminAccessChecking(userId);

        // Already Food Item Available or Not Condition Checking
        FoodItem existItem = foodItemRepository.findById(foodItemId)
                .orElseThrow(() -> new DynamoException("Food Item is Not Available", HttpStatus.NOT_FOUND));

        // Delete this Food Item
        foodItemRepository.deleteById(existItem.getFoodItemId());
    }
}
