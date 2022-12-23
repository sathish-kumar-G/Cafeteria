package net.breezewere.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import net.breezewere.dto.FoodItems;
import net.breezewere.dto.FoodMenuDto;
import net.breezewere.dto.FoodMenuUpdateDto;
import net.breezewere.dto.FoodMenuViewDto;
import net.breezewere.dto.FoodMenuViewUserDto;
import net.breezewere.entity.FoodItem;
import net.breezewere.entity.FoodMenu;
import net.breezewere.entity.FoodMenuItemMap;
import net.breezewere.entity.Role;
import net.breezewere.entity.User;
import net.breezewere.entity.UserRoleMap;
import net.breezewere.exception.CustomException;
import net.breezewere.repository.FoodItemRepository;
import net.breezewere.repository.FoodMenuItemMapRepository;
import net.breezewere.repository.FoodMenuRepository;
import net.breezewere.repository.RoleRepository;
import net.breezewere.repository.UserRepository;
import net.breezewere.repository.UserRoleMapRepository;

@SpringBootTest
@DisplayName("Food Menu Test")
class FoodMenuServiceImplTest {

    @Mock
    FoodMenuRepository repository;

    @Mock
    FoodItemRepository foodItemRepository;

    @Mock
    FoodMenuItemMapRepository foodMenuItemMapRepository;

    @Mock
    UserRoleMapRepository userRoleMapRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    FoodMenuServiceImpl service;

    // Food Menu Instance For All Test cases
    FoodMenu foodMenu;
    User user;

    @BeforeEach
    void setUp() throws Exception {
        foodMenu = new FoodMenu();
        user = new User();
    }

    // Create Food Menu Test Case
    @Test
    @DisplayName("Create Food Menu Test")
    void testCreateFoodMenu() throws CustomException {

        // Admin Access Checked using Private Method
        adminAccessChecking(user.getUserId());

        FoodMenuDto foodItem = new FoodMenuDto();
        foodMenu.setFoodMenuId(1);
        foodMenu.setFoodMenuName("Indian");
        foodMenu.setFoodMenuType("veg");
        foodMenu.setStatus("active");

        FoodItems foodItems = new FoodItems();
        foodItems.setFoodItemCount(20);

        List<FoodItems> list = new ArrayList<>();
        list.add(foodItems);

        foodItem.setFoodMenu(foodMenu);

        foodItem.setFoodItems(list);

        FoodItem item = new FoodItem();
        when(foodItemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        when(repository.save(any(FoodMenu.class))).thenReturn(foodMenu);

        FoodMenu actual = service.createFoodMenu(foodItem, user.getUserId());

        assertEquals(foodMenu, actual);

    }

    private void adminAccessChecking(long userId) {

        // Admin Access case
        User user = new User();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        UserRoleMap map = new UserRoleMap();
        Role role = new Role();
        map.setRoleId(role);
        map.getRoleId().setRoleId(1);
        when(userRoleMapRepository.findByUserId(user)).thenReturn(Optional.of(map));
    }

    // Create Food Menu Custom Exception throw Test case
    @Test
    @DisplayName("Exception Test Case")
    void testCustomException_nullValue() throws CustomException {
        FoodMenuDto dto = new FoodMenuDto();
        foodMenu.setFoodMenuId(1);
        foodMenu.setFoodMenuName("Indian");
        foodMenu.setFoodMenuType("veg");
        foodMenu.setStatus("");

        dto.setFoodMenu(foodMenu);

        assertThrows(CustomException.class, () -> {
            service.createFoodMenu(dto, user.getUserId());
        });
    }

    // Get Food Menu By Id Test case
    @Test
    @DisplayName("Get All Food Menu")
    void testGetFoodMenuById() throws CustomException {

        // Admin Access Checked using Private Method
        adminAccessChecking(user.getUserId());

        List<FoodMenuItemMap> maps = new ArrayList<>();

        FoodMenuItemMap map = new FoodMenuItemMap();
        List<FoodItem> listFoodItem = new ArrayList<>();

        FoodItem foodItem = new FoodItem();

        listFoodItem.add(foodItem);

        when(repository.findById(anyLong())).thenReturn(Optional.of(foodMenu));

        map.setFoodMenu(foodMenu);
        map.setFoodItem(foodItem);

        FoodMenuViewDto foodMenuViewDto = new FoodMenuViewDto();
        foodMenuViewDto.setFoodMenu(foodMenu);
        foodMenuViewDto.setFoodItems(listFoodItem);
        maps.add(map);
        when(foodMenuItemMapRepository.findByFoodMenu(any(FoodMenu.class))).thenReturn(maps);
        FoodMenuViewDto dto = service.getFoodMenuById(foodMenu.getFoodMenuId(), user.getUserId());

        assertEquals(dto, foodMenuViewDto);
    }

    // Update Food Menu Test Case
    @Test
    @DisplayName("Update Food Menu")
    void testUpdateFoodMenu() throws CustomException {

        // Admin Access Checked using Private Method
        adminAccessChecking(user.getUserId());

        when(repository.findById(anyLong())).thenReturn(Optional.of(foodMenu));

        FoodItems foodItem = new FoodItems();
        FoodMenuUpdateDto dto = new FoodMenuUpdateDto();
        FoodItem item = new FoodItem();

        FoodMenuItemMap foodMenuItemMap = new FoodMenuItemMap();
        foodMenuItemMap.setFoodItem(item);
        when(foodMenuItemMapRepository.findByFoodMenuAndFoodItemFoodItemId(foodMenu, foodItem.getFoodItemId()))
                .thenReturn(Optional.of(foodMenuItemMap));

        List<FoodItems> foodItems = new ArrayList<>();
        foodItems.add(foodItem);

        when(foodItemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        dto.setFoodItem(foodItems);

        FoodMenuUpdateDto foodMenuUpdateDto = service.updateFoodMenu(dto, foodMenu.getFoodMenuId(), user.getUserId());
        assertEquals(dto, foodMenuUpdateDto);
    }

    // User Get All Active Food Menu with Food Items
    @Test
    @DisplayName("Get All Active Food Menu by User")
    void testGetAllActiveFoodMenu() throws CustomException {

        // user case
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        Role role = new Role();
        role.setRoleId(3);
        UserRoleMap map = new UserRoleMap();
        map.setRoleId(role);
        when(userRoleMapRepository.findByUserId(user)).thenReturn(Optional.of(map));

        List<FoodMenu> foodMenus = new ArrayList<>();
        when(repository.findAll()).thenReturn(foodMenus);

        List<FoodMenuItemMap> foodMenuItemMaps = new ArrayList<>();
        when(foodMenuItemMapRepository.findByFoodMenu(foodMenu)).thenReturn(foodMenuItemMaps);

        List<FoodMenuViewUserDto> foodMenuViewUserDtos = new ArrayList<>();

        List<FoodMenuViewUserDto> actual = service.getAllActiveFoodMenu(user.getUserId());
        assertEquals(foodMenuViewUserDtos, actual);
    }
}
