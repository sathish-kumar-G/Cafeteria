package net.breezeware.service;

import static net.breezeware.enumeration.OrderStatus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.breezeware.dynamo.utils.exception.DynamoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import net.breezeware.dto.AddressDto;
import net.breezeware.dto.OrderDto;
import net.breezeware.dto.OrderFoodItems;
import net.breezeware.dto.OrderUpdateDto;
import net.breezeware.dto.OrderViewDto;
import net.breezeware.dto.ViewUserOrderFoodItem;
import net.breezeware.entity.FoodItem;
import net.breezeware.entity.FoodMenuItemMap;
import net.breezeware.entity.Order;
import net.breezeware.entity.OrderAddressMap;
import net.breezeware.entity.OrderFoodItemMap;
import net.breezeware.entity.Role;
import net.breezeware.entity.User;
import net.breezeware.entity.UserRoleMap;
import net.breezeware.exception.CustomException;
import net.breezeware.repository.FoodItemRepository;
import net.breezeware.repository.FoodMenuItemMapRepository;
import net.breezeware.repository.OrderAddressMapRepository;
import net.breezeware.repository.OrderFoodItemMapRepository;
import net.breezeware.repository.OrderRepository;
import net.breezeware.repository.UserRepository;
import net.breezeware.repository.UserRoleMapRepository;

@SpringBootTest
@DisplayName("Order Test Case")
public class OrderServiceImplTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    UserRoleMapRepository userRoleMapRepository;

    @Mock
    FoodItemRepository foodItemRepository;

    @Mock
    FoodMenuItemMapRepository foodMenuItemMapRepository;

    @Mock
    OrderFoodItemMapRepository orderFoodItemMapRepository;

    @Mock
    OrderAddressMapRepository orderAddressMapRepository;

    @InjectMocks
    OrderServiceImpl orderService;

    // Create Instance For class
    Order order;
    User user;
    FoodItem foodItem;
    FoodMenuItemMap foodMenuItemMap;
    OrderFoodItemMap orderFoodItemMap;

    // Before Each init method for create instance for every test case
    @BeforeEach
    void setupInit() throws DynamoException {
        order = new Order();
        user = new User();
        foodItem = new FoodItem();
        foodMenuItemMap = new FoodMenuItemMap();
        orderFoodItemMap = new OrderFoodItemMap();
    }

    // Test for Add Food Items to the Order
    @Test
    @DisplayName("Add Food Items to the Order Test case")
    void testAddFoodItemsToOrder() throws DynamoException {

        // Customer Access Checking case by private method
        customerAccessChecking(user);

        // Create Object For OrderDto and set the value
        OrderDto orderDto = new OrderDto();
        OrderFoodItems orderFoodItems = new OrderFoodItems();
        List<OrderFoodItems> orderFoodItemList = new ArrayList<>();
        orderFoodItemList.add(orderFoodItems);
        orderDto.setOrderFoodItems(orderFoodItemList);
        orderDto.setOrderFoodItems(orderFoodItemList);

        // Food Item case
        when(foodItemRepository.findById(anyLong())).thenReturn(Optional.of(foodItem));

        // Food Menu Item case
        when(foodMenuItemMapRepository.findByFoodItem(foodItem)).thenReturn(Optional.of(foodMenuItemMap));

        // Order save case
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Order Food Item Map
        when(orderFoodItemMapRepository.save(orderFoodItemMap)).thenReturn(orderFoodItemMap);

        // Actual value
        when(orderRepository.save(order)).thenReturn(order);
        Order actualOrder = orderService.addFoodItemsToOrder(orderDto, user.getUserId());

        // Check
        assertEquals(order, actualOrder);

    }

    // View User Orders By User Id Test case
    @Test
    @DisplayName("View User Orders Test Case")
    void testViewUserOrder() throws DynamoException {

        // Customer Access Checking case by private method
        customerAccessChecking(user);

        // Get List of Orders for this User
        order.setUser(user);
        List<Order> orderList = new ArrayList<>();
        orderList.add(order);
        when(orderRepository.findByUser(user)).thenReturn(orderList);

        // Get the List of Orders
        List<OrderViewDto> userViewDtos = new ArrayList<>();
        // Get the Completed Order Details using Private Method
        OrderViewDto userViewDto = getOrderDetails();
        // Set Address Null because Address is not Shown
        userViewDto.setAddress(null);
        userViewDtos.add(userViewDto);

        // Actual Result
        List<OrderViewDto> actual = orderService.viewUserOrder(user.getUserId());

        assertEquals(userViewDtos, actual);
    }

    // Update the Order by User Test case
    @Test
    @DisplayName("Update the Order by User Test case")
    void testUpdateTheOrderByUserId() throws DynamoException {

        // Customer Access And Order case using Private Method
        customerAndOrderChecking(user, order);

        // Get the FoodItem for this Order
        ViewUserOrderFoodItem foodItemList = new ViewUserOrderFoodItem();
        foodItemList.setFoodItemId(1);
        foodItemList.setQty(3);
        List<ViewUserOrderFoodItem> foodItemLists = new ArrayList<>();
        foodItemLists.add(foodItemList);

        // Food Item case
        foodItem.setFoodItemId(1);
        when(foodItemRepository.findById(foodItemList.getFoodItemId())).thenReturn(Optional.of(foodItem));

        // Order Food Item map case
        orderFoodItemMap.setFoodItem(foodItem);
        orderFoodItemMap.setOrder(order);
        orderFoodItemMap.setQuantity(foodItemList.getQty());
        when(orderFoodItemMapRepository.findByOrderAndFoodItemFoodItemId(order, foodItemList.getFoodItemId()))
                .thenReturn(Optional.of(orderFoodItemMap));

        // Food Menu Item map case
        foodMenuItemMap.setFoodCount(100);
        foodMenuItemMap.setFoodCount(foodMenuItemMap.getFoodCount() - foodItemList.getQty());
        when(foodMenuItemMapRepository.findByFoodItem(foodItem)).thenReturn(Optional.of(foodMenuItemMap));

        // set The Expected Data
        OrderUpdateDto dto = new OrderUpdateDto();
        dto.setFoodItem(foodItemLists);

        OrderUpdateDto actual = orderService.updateTheOrderByUserId(dto, user.getUserId(), order.getOrderId());

        assertEquals(dto, actual);

    }

    @Test
    @DisplayName("Place an Order By User Test case")
    void testPlaceAnOrderByUser() throws DynamoException {

        // Customer Access And Order case using Private Method
        customerAndOrderChecking(user, order);

        // Set the Address Details
        OrderAddressMap addressMap = new OrderAddressMap();
        addressMap.setDoorNo("48b");
        addressMap.setStreet("street");
        addressMap.setCity("coimbatore");
        addressMap.setState("tamilnadu");
        addressMap.setPhoneNumber("9876789765");
        addressMap.setOrder(order);
        when(orderAddressMapRepository.save(addressMap)).thenReturn(addressMap);

        OrderAddressMap actual = orderService.placeAnOrderByUser(addressMap, user.getUserId(), order.getOrderId());

        assertEquals(addressMap, actual);

    }

    // Exception Test case
    @Test
    @DisplayName("Exception Test case Null Value Occure in testPlaceAnOrderByUser case")
    void testPlaceAnOrderByUser_withNullInput_throwsCustomException() throws DynamoException {
        assertThrows(DynamoException.class, () -> {
            orderService.placeAnOrderByUser(null, 1, 1);
        });
    }

    // View All Active Orders by Staff
    @Test
    @DisplayName("View All Active Orders By Staff Test case")
    void testGetTheListOfActiveOrdersByStaff() throws DynamoException {

        // Staff Access case using private Method
        staffAccessChecking(user);

        // Find All Order case
        order.setStatus(PLACED.getStatus());
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        when(orderRepository.findAll()).thenReturn(orders);

        // Get the List of Orders
        List<OrderViewDto> userViewDtos = new ArrayList<>();
        // Get the Active Order Details using Private Method
        OrderViewDto userViewDto = getOrderDetails();
        userViewDtos.add(userViewDto);

        // Actual Result
        List<OrderViewDto> actual = orderService.getTheListOfActiveOrdersByStaff(user.getUserId());

        assertEquals(userViewDtos, actual);

    }

    // Staff view the Received Order By Id
    @Test
    @DisplayName("Staff view the Received Order By Id Test case")
    void testViewtheReceivedOrderByStaff() throws DynamoException {

        // Staff Access case using private Method
        staffAccessChecking(user);

        // Order case
        order.setStatus(PLACED.getStatus());
        when(orderRepository.findById(order.getOrderId())).thenReturn(Optional.of(order));

        // Find All Order case
        order.setStatus(PLACED.getStatus());
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        when(orderRepository.findAll()).thenReturn(orders);

        // Get the Received Order Details using Private Method
        OrderViewDto userViewDto = getOrderDetails();
        userViewDto.setStatus(RECEIVED.getStatus());

        OrderViewDto actual = orderService.viewTheReceivedOrderByStaff(user.getUserId(), order.getOrderId());

        assertEquals(userViewDto, actual);

    }

    // View List Of Cancelled Orders by Staff Test case
    @Test
    @DisplayName("View List of Cancel Orders By Staff case")
    void testViewListOfCancelOrdersByStaff() throws DynamoException {

        // Staff Access case using private Method
        staffAccessChecking(user);

        // Find All Order case
        order.setStatus(CANCEL.getStatus());
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        when(orderRepository.findAll()).thenReturn(orders);

        // Get the List of Orders
        List<OrderViewDto> userViewDtos = new ArrayList<>();
        // Get the cancel Order Details using Private Method
        OrderViewDto userViewDto = getOrderDetails();
        userViewDtos.add(userViewDto);

        // Actual Result
        List<OrderViewDto> actual = orderService.viewListOfCancelOrdersByStaff(user.getUserId());

        assertEquals(userViewDtos, actual);

    }

    // View the Cancel Order by Staff using Order Id Test case
    @Test
    @DisplayName("View the Cancel Order by Staff Test case")
    void testViewCancelOrderByStaff() throws DynamoException {

        // Staff Access case using private Method
        staffAccessChecking(user);

        // Order case
        order.setStatus(CANCEL.getStatus());
        when(orderRepository.findById(order.getOrderId())).thenReturn(Optional.of(order));

        // Find All Order case
        order.setStatus(CANCEL.getStatus());
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        when(orderRepository.findAll()).thenReturn(orders);

        // Get the Cancelled Order Details using Private Method
        OrderViewDto userViewDto = getOrderDetails();

        OrderViewDto actual = orderService.viewCancelOrderByStaff(user.getUserId(), order.getOrderId());

        assertEquals(userViewDto, actual);
    }

    // View List Of Completed Orders By Staff Test case
    @Test
    @DisplayName("View List of Completed Orders by Staff")
    void testViewListOfCompletedOrdersByStaff() throws DynamoException {

        // Staff Access case using private Method
        staffAccessChecking(user);

        // Find All Order case
        order.setStatus(DELIVERED.getStatus());
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        when(orderRepository.findAll()).thenReturn(orders);

        // Get the List of Orders
        List<OrderViewDto> userViewDtos = new ArrayList<>();
        // Get the Completed Order Details using Private Method
        OrderViewDto userViewDto = getOrderDetails();
        userViewDtos.add(userViewDto);

        // Actual Result
        List<OrderViewDto> actual = orderService.viewListOfCompletedOrdersByStaff(user.getUserId());

        assertEquals(userViewDtos, actual);
    }

    // View Completed Order by Staff using Order Id case
    @Test
    @DisplayName("View Completed Order Test case")
    void testViewCompletedOrderByStaff() throws DynamoException {

        // Staff Access case using private Method
        staffAccessChecking(user);

        // Order case
        order.setStatus(DELIVERED.getStatus());
        when(orderRepository.findById(order.getOrderId())).thenReturn(Optional.of(order));

        // Find All Order case
        order.setStatus(DELIVERED.getStatus());
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        when(orderRepository.findAll()).thenReturn(orders);

        // Get the Completed Order Details using Private Method
        OrderViewDto userViewDto = getOrderDetails();

        OrderViewDto actual = orderService.viewCompletedOrderByStaff(user.getUserId(), order.getOrderId());

        assertEquals(userViewDto, actual);
    }

    // Staff Access private Method
    private void staffAccessChecking(User user) {

        // User case
        user.setUserId(1);
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        // Set the Role
        Role role = new Role();
        role.setRoleId(2);

        // UserRoleMap case
        UserRoleMap userRoleMap = new UserRoleMap();
        userRoleMap.setRoleId(role);
        when(userRoleMapRepository.findByUserId(user)).thenReturn(Optional.of(userRoleMap));

       // return user;
    }

    // Customer Access Private Method
    private void customerAccessChecking(User user) {

        // User case
        user.setUserId(1);
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        // User Role Map case
        Role role = new Role();
        role.setRoleId(3);
        UserRoleMap userRoleMap = new UserRoleMap();
        userRoleMap.setRoleId(role);
        when(userRoleMapRepository.findByUserId(user)).thenReturn(Optional.of(userRoleMap));

    }

    // Customer Access and Order Checking Private method
    private void customerAndOrderChecking(User user, Order order) {

        // Customer Access case using private Method
        customerAccessChecking(user);

        // Order case
        order.setOrderId(1);
        order.setStatus(PROCESS.getStatus());
        when(orderRepository.findById(order.getOrderId())).thenReturn(Optional.of(order));

        // Order User Case
        when(orderRepository.findByOrderIdAndUser(order.getOrderId(), user)).thenReturn(Optional.of(order));
    }

    // Get Order Details using Private Method
    private OrderViewDto getOrderDetails() {
        // Order case
        when(orderRepository.findById(order.getOrderId())).thenReturn(Optional.of(order));

        // Create List for Get the List Of Food Items for this Order
        List<OrderFoodItemMap> orderFoodItemList = new ArrayList<>();
        orderFoodItemMap.setFoodItem(foodItem);
        orderFoodItemMap.setQuantity(1);
        orderFoodItemList.add(orderFoodItemMap);
        when(orderFoodItemMapRepository.findByOrder(order)).thenReturn(orderFoodItemList);

        // Order address Map case
        OrderAddressMap orderAddressMap = new OrderAddressMap();
        when(orderAddressMapRepository.findByOrder(order)).thenReturn(Optional.of(orderAddressMap));

        // Create List for Get the List of Food Items with quantity
        ViewUserOrderFoodItem foodItemAndQuantity = new ViewUserOrderFoodItem();
        foodItemAndQuantity.setQty(1);
        List<ViewUserOrderFoodItem> foodItemAndQuantityList = new ArrayList<>();
        foodItemAndQuantityList.add(foodItemAndQuantity);

        // Get List of Orders with food items for this User
        OrderViewDto userViewDto = new OrderViewDto();
        userViewDto.setFoodItem(foodItemAndQuantityList);
        userViewDto.setStatus(order.getStatus());

        // Set the Address Details
        AddressDto addressDto = new AddressDto();
        userViewDto.setAddress(addressDto);

        return userViewDto;
    }

}
