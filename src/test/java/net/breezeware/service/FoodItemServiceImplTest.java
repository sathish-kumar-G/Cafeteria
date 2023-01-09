package net.breezeware.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import net.breezeware.entity.FoodItem;
import net.breezeware.entity.Role;
import net.breezeware.entity.User;
import net.breezeware.entity.UserRoleMap;
import net.breezeware.exception.CustomException;
import net.breezeware.repository.FoodItemRepository;
import net.breezeware.repository.UserRepository;
import net.breezeware.repository.UserRoleMapRepository;

@SpringBootTest
@DisplayName("Food Item Test")
class FoodItemServiceImplTest {

    @Mock
    FoodItemRepository repository;

    @Mock
    UserRoleMapRepository mapRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    FoodItemServiceImpl service;

    // create Instance for All Test case
    FoodItem foodItem;
    User user;

    @BeforeEach
    void setUp() throws Exception {
        foodItem = new FoodItem();
        user = new User();
    }

    // Create Food Item Test
    @Test
    @DisplayName("create Food item Test")
    void testCreateFoodItem() throws CustomException {

        // Admin Access Checking using Private Method
        adminAccessChecking(user.getUserId());

        foodItem.setFoodName("Pizza");
        foodItem.setFoodPrice(500);

        when(repository.save(any(FoodItem.class))).thenReturn(foodItem);

        FoodItem saveItem = service.createFoodItem(foodItem, user.getUserId());

        assertEquals(saveItem, foodItem);
      //  assertNotNull(saveItem);

    }

    // create food item Exception Test case
    @Test
    @DisplayName("create food item exception test")
    void testCreateFoodItem_nullPointerExceptionTest() {

        foodItem.setFoodName("");
        foodItem.setFoodPrice(100);

        assertThrows(CustomException.class, () -> {
            service.createFoodItem(foodItem, anyLong());
        });

    }

    // view All Food Item Test case
    @Test
    @DisplayName("View All Food Items")
    void testViewAllFoodItems() throws CustomException {

        // Admin Access Checking using Private Method
        adminAccessChecking(user.getUserId());

        List<FoodItem> foodItems = new ArrayList<>();

        foodItems.add(foodItem);

        when(repository.findAll()).thenReturn(foodItems);

        List<FoodItem> findAll = service.viewAllFoodItem(anyLong());

        assertEquals(foodItems, findAll);
    }

    // Update Food Item Test case
    @Test
    @DisplayName("Update Food Item ")
    void testUpdateFoodItemById() throws CustomException {

        // Admin Access Checking using Private Method
        adminAccessChecking(user.getUserId());

        when(repository.findById(anyLong())).thenReturn(Optional.of(foodItem));

        foodItem.setFoodName("pizza");
        foodItem.setFoodPrice(300);

        User user = new User();

        when(repository.save(any(FoodItem.class))).thenReturn(foodItem);

        FoodItem updateItem = service.updateFoodItemById(foodItem, anyLong(), user.getUserId());

        assertEquals(updateItem, foodItem);

    }

    // Delete Food item test case
    @Test
    @DisplayName("Delete Food Item ")
    void testDeleteFoodItemById() throws CustomException {

        repository.save(foodItem);

        repository.deleteById(foodItem.getFoodItemId());

        Optional<FoodItem> deleteId = repository.findById(foodItem.getFoodItemId());
        assertThat(deleteId).isEmpty();

    }

    // Private Method For Admin Access case
    private void adminAccessChecking(long userId) {

        UserRoleMap map = new UserRoleMap();
        Role role = new Role();
        role.setRoleId(1);
        map.setRoleId(role);
        User user = new User();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        when(mapRepository.findByUserId(user)).thenReturn(Optional.of(map));
    }

}
