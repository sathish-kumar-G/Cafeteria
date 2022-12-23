package net.breezewere.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import net.breezewere.dto.AddressDto;
import net.breezewere.dto.OrderDto;
import net.breezewere.dto.OrderFoodItems;
import net.breezewere.dto.OrderUpdateDto;
import net.breezewere.dto.OrderViewDto;
import net.breezewere.dto.ViewUserOrderFoodItemList;
import net.breezewere.entity.FoodItem;
import net.breezewere.entity.FoodMenuItemMap;
import net.breezewere.entity.Order;
import net.breezewere.entity.OrderAddressMap;
import net.breezewere.entity.OrderFoodItemMap;
import net.breezewere.entity.User;
import net.breezewere.entity.UserRoleMap;
import net.breezewere.exception.CustomException;
import net.breezewere.repository.FoodItemRepository;
import net.breezewere.repository.FoodMenuItemMapRepository;
import net.breezewere.repository.OrderAddressMapRepository;
import net.breezewere.repository.OrderFoodItemMapRepository;
import net.breezewere.repository.OrderRepository;
import net.breezewere.repository.UserRepository;
import net.breezewere.repository.UserRoleMapRepository;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderFoodItemMapRepository orderFoodItemMapRepository;

    @Autowired
    private FoodMenuItemMapRepository foodMenuItemMapRepository;

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleMapRepository userRoleMapRepository;

    @Autowired
    private OrderAddressMapRepository orderAddressMapRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Order addFoodItemsToOrder(@Valid OrderDto orderDto, long userId) throws CustomException {

        // Check User is Available or not and Customer Access Checking
        User user = customerAccessChecking(userId);

        // Initialize Order class and amount variable
        Order order = new Order();
        long amount = 0;

        // For each loop is used for store the each Food Items
        for (OrderFoodItems orderItems : orderDto.getOrderFoodItems()) {

            // Check Food Item is Available or not, and Get Food Items
            FoodItem foodItem = foodItemRepository.findById(orderItems.getFoodItemId())
                    .orElseThrow(() -> new CustomException("Food Item is not Available", HttpStatus.NOT_FOUND));

            // Check Food Item is Available or not, and Get Food Menu Item Map
            FoodMenuItemMap foodMenuItemMap = foodMenuItemMapRepository.findByFoodItem(foodItem)
                    .orElseThrow(() -> new CustomException("Food Item  not Available", HttpStatus.NOT_FOUND));

            // Check Food Item Count
            if (foodMenuItemMap.getFoodCount() < orderItems.getQty()) {
                throw new CustomException("Food Item Quantity is not Sufficient", HttpStatus.SERVICE_UNAVAILABLE);
            }

            // Update the Food Count
            foodMenuItemMap.setFoodCount(foodMenuItemMap.getFoodCount() - orderItems.getQty());

            // Save the Updated Food Count in Food Menu Item Map table
            foodMenuItemMapRepository.save(foodMenuItemMap);

            // set the Data in Order table
            order.setStatus("process");
            amount = amount + (foodItem.getFoodPrice() * orderItems.getQty());
            order.setAmount(amount);
            order.setUser(user);

            // Private method used for save the Order Food Item Map table records
            saveOrderFoodItemMap(foodItem, order, orderItems.getQty());

        }

        return orderRepository.save(order);
    }

    // Private Method is created for save the Order Food Item Map table records
    private OrderFoodItemMap saveOrderFoodItemMap(FoodItem foodItem, Order order, long foodItemQty) {
        OrderFoodItemMap orderFoodItemMap = new OrderFoodItemMap();
        orderFoodItemMap.setFoodItem(foodItem);
        orderFoodItemMap.setOrder(order);
        orderFoodItemMap.setQuantity(foodItemQty);
        return orderFoodItemMapRepository.save(orderFoodItemMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OrderViewDto> viewUserOrder(long userId) throws CustomException {

        // Check User is Available or not and Customer Access Checking
        User user = customerAccessChecking(userId);

        // Get List of Orders for this User
        List<Order> orderList = orderRepository.findByUser(user);

        // Get the List of Orders for this User using private method
        List<OrderViewDto> viewCustomerOrder = getOrders(orderList);

        // return the All Orders for the Customer
        return viewCustomerOrder;

    }

    /**
     * {@inheritDoc}
     * @throws CustomException
     */
    @Override
    public OrderUpdateDto updateTheOrderByUserId(OrderUpdateDto orderUpdateDto, long userId, long orderId)
            throws CustomException {

        // Check User is Available or not and Customer Access Checking using Private
        // Method
        User user = customerAccessChecking(userId);

        // Check Order is Available or not
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order is Not Available", HttpStatus.NOT_FOUND));

        // Check This User have this Order
        orderRepository.findByOrderIdAndUser(order.getOrderId(), user).orElseThrow(
                () -> new CustomException("This Order is not Available For this User", HttpStatus.NOT_FOUND));

        // Declare amount for Get Total amount in Order
        long amount = 0;

        // For each loop is used for Update the Food Items in the Order
        for (ViewUserOrderFoodItemList foodItemList : orderUpdateDto.getFoodItem()) {

            // Check Food Item is Available and Get Food item for this order
            FoodItem foodItem = foodItemRepository.findById(foodItemList.getFoodItemId())
                    .orElseThrow(() -> new CustomException("Food Item is not Available", HttpStatus.NOT_FOUND));

            // Set the Food Item details in Dto for Get Output JSON data
            foodItemList.setFoodName(foodItem.getFoodName());
            foodItemList.setFoodPrice(foodItem.getFoodPrice());

            // Check Food Items For this Order and Get Food Items
            OrderFoodItemMap foodItemMap =
                    orderFoodItemMapRepository.findByOrderAndFoodItemFoodItemId(order, foodItemList.getFoodItemId())
                            .orElseThrow(() -> new CustomException("Food Item is not Available For this Order",
                                    HttpStatus.NOT_MODIFIED));
            // System.out.println(foodItemMap);

            // Check Food Item is Available or not, and Get Food Menu Item Map
            FoodMenuItemMap foodMenuItemMap = foodMenuItemMapRepository.findByFoodItem(foodItem)
                    .orElseThrow(() -> new CustomException("Food Item is not Available", HttpStatus.NOT_FOUND));

            // Update the Food Count in Food Menu Map Table
            foodMenuItemMap.setFoodCount(foodMenuItemMap.getFoodCount() + foodItemMap.getQuantity());

            // Save the Updated Food Count in Food Menu Item Map table
            foodMenuItemMapRepository.save(foodMenuItemMap);

            // Check Food Item Count is Sufficient or not
            if (foodMenuItemMap.getFoodCount() < foodItemList.getQty()) {
                throw new CustomException("Food Item Quantity is not Sufficiant", HttpStatus.INSUFFICIENT_STORAGE);
            }

            // Update the Food Count in Food Menu Map Table
            foodMenuItemMap.setFoodCount(foodMenuItemMap.getFoodCount() - foodItemList.getQty());

            // Save the Updated Food Count in Food Menu Item Map table
            foodMenuItemMapRepository.save(foodMenuItemMap);

            // System.out.println(foodMenuItemMap.getFoodCount());
            // System.out.println(updateQty);

            // Set and Save the FoodItem Quantity in Order Food Item Map table
            foodItemMap.setQuantity(foodItemList.getQty());
            orderFoodItemMapRepository.save(foodItemMap);

            // Find the Total amount of FoodItems in this Order and Set it
            amount = amount + (foodItem.getFoodPrice() * foodItemList.getQty());
            order.setAmount(amount);
            // order.setUser(user);

        }

        // set the total amount for this Order to display the data
        orderUpdateDto.setAmount(amount);
        // Save the Updated Order in Order table
        orderRepository.save(order);
        // return the Updated Food Items data
        return orderUpdateDto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderAddressMap placeAnOrderByUser(OrderAddressMap orderAddressMap, long userId, long orderId)
            throws CustomException {

        // Check User is Available or not and Customer Access Checking
        User user = customerAccessChecking(userId);

        // Check Order is Available or not and Get the Order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order is Not Available", HttpStatus.NOT_FOUND));

        // Check Whether User Have this Order or not
        Order orderUser = orderRepository.findByOrderIdAndUser(orderId, user)
                .orElseThrow(() -> new CustomException("This User is not Having this Order", HttpStatus.UNAUTHORIZED));

        // Check The Order is Already placed or Cancelled
        if (orderUser.getStatus().equals("Order Placed") || orderUser.getStatus().equals("Order Cancelled")) {
            throw new CustomException("The Order is Already Placed/Cancelled", HttpStatus.CONFLICT);
        }

        // Check value is Empty
        if (orderAddressMap.getDoorNo().isEmpty() || orderAddressMap.getDoorNo().isBlank()) {
            throw new CustomException("Please Fill the Door number", HttpStatus.BAD_REQUEST);
        }

        if (orderAddressMap.getStreet().isEmpty() || orderAddressMap.getStreet().isBlank()) {
            throw new CustomException("Please Fill the Street details", HttpStatus.BAD_REQUEST);
        }

        if (orderAddressMap.getCity().isEmpty() || orderAddressMap.getCity().isBlank()) {
            throw new CustomException("Please Fill the City Name", HttpStatus.BAD_REQUEST);
        }

        if (orderAddressMap.getState().isEmpty() || orderAddressMap.getState().isBlank()) {
            throw new CustomException("Please Fill the State Name", HttpStatus.BAD_REQUEST);
        }

        if (orderAddressMap.getPhoneNumber().isEmpty() || orderAddressMap.getPhoneNumber().isBlank()
                || orderAddressMap.getPhoneNumber().length() != 10) {
            throw new CustomException("Please Fill the Correct Phone number", HttpStatus.BAD_REQUEST);
        }

        // Set and Save the Order Status
        order.setStatus("Order Placed");
        orderRepository.save(order);

        // Set Order Details in OrderAddress Map table
        orderAddressMap.setOrder(order);

        // Set the Current Date in Ordered Date
        long millis = System.currentTimeMillis();
        Date date = new Date(millis);
        orderAddressMap.setOrderDate(date);

        // Save the Details in Order Address Map Table
        return orderAddressMapRepository.save(orderAddressMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancelTheOrderByUser(long userId, long orderId) throws CustomException {

        // Check User is Available or not and Customer Access Checking
        User user = customerAccessChecking(userId);

        // Check Order is Available or Not
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order is Not Available", HttpStatus.NOT_FOUND));

        // Check Whether User Have this Order or not
        Order orderUser = orderRepository.findByOrderIdAndUser(order.getOrderId(), user)
                .orElseThrow(() -> new CustomException("This User is not Having this Order", HttpStatus.UNAUTHORIZED));

        // Check the Order is Process/Cancelled
        if (orderUser.getStatus().equals("process") || orderUser.getStatus().equals("Cancelled")
                || orderUser.getStatus().equals("Order Deliverd")) {
            throw new CustomException("Order is not Eligible for Cancel or Already Cancelled", HttpStatus.NOT_FOUND);
        }

        // Get Order with List of Food Items
        List<OrderFoodItemMap> orderFoodItemMaps = orderFoodItemMapRepository.findByOrder(orderUser);

        // For each loop is used for Change the Food Item Count in Food Menu Item Map
        // Table
        for (OrderFoodItemMap orderFoodItemMap : orderFoodItemMaps) {

            // Get the Order Food Item
            FoodMenuItemMap foodMenuItemMap =
                    foodMenuItemMapRepository.findByFoodItem(orderFoodItemMap.getFoodItem()).orElseThrow();
            // Update and Save the Count in Food Menu Item Map
            foodMenuItemMap.setFoodCount(foodMenuItemMap.getFoodCount() + orderFoodItemMap.getQuantity());
            foodMenuItemMapRepository.save(foodMenuItemMap);

        }

        // Update and Save the Order Status
        orderUser.setStatus("Cancelled");
        orderRepository.save(orderUser);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OrderViewDto> getTheListOfActiveOrdersByStaff(long userId) throws CustomException {

        // Check Staff Access Using Private Method
        staffAccessChecking(userId);

        // Get List of Orders for this User
        List<Order> orderList = orderRepository.findAll();

        // Get the Active Orders List
        List<OrderViewDto> activeOrders = new ArrayList<>();
        // Get the All Orders using Private method getOrders
        List<OrderViewDto> allOrders = getOrders(orderList);
        // for Each loop is used for get the Active Orders
        for (OrderViewDto orders : allOrders) {

            // Check the Order is Active or not
            if (orders.getStatus().equals("Order Placed")) {
                // Set the Order Address Details using Private method getOrderId()
                orders.setAddress(setAddress(orders.getOrderId()));
                // Add the Active Orders
                activeOrders.add(orders);

            }

        }

        // return the list Of Active Orders with Food Items
        return activeOrders;
    }

    // Get List of Orders using Private Method
    private List<OrderViewDto> getOrders(List<Order> orderList) throws CustomException {

        // Get List of Orders with food items for this User
        List<OrderViewDto> userViewDtos = new ArrayList<>();

        // For each loop is used for Get the each Order with Food Items
        for (Order order : orderList) {

            // Create List for Get the List Of Food Items for this Order
            List<OrderFoodItemMap> orderFoodItemList = orderFoodItemMapRepository.findByOrder(order);

            // Create instance for Dto class for Get the Orders with Food Items
            OrderViewDto userViewDto = new OrderViewDto();
            // Set the Order details in Dto
            userViewDto.setOrderId(order.getOrderId());
            userViewDto.setStatus(order.getStatus());
            userViewDto.setAmount(order.getAmount());

            // Create List for Get the List of Food Items with quantity
            List<ViewUserOrderFoodItemList> foodItemAndQuantityList = new ArrayList<>();

            // For each loop is used for Get the List of Food Items for this Order
            for (OrderFoodItemMap foodItems : orderFoodItemList) {

                // Create instance for set Food Items in dto
                ViewUserOrderFoodItemList foodItemAndQuantity = new ViewUserOrderFoodItemList();

                // if Food Item Qty is zero then Not Shown
                if (foodItems.getQuantity() != 0) {
                    // Set the FoodItems values in the Dto
                    foodItemAndQuantity.setFoodItemId(foodItems.getFoodItem().getFoodItemId());
                    foodItemAndQuantity.setFoodName(foodItems.getFoodItem().getFoodName());
                    foodItemAndQuantity.setFoodPrice(foodItems.getFoodItem().getFoodPrice());
                    foodItemAndQuantity.setQty(foodItems.getQuantity());

                    // Add the FoodItems value in List
                    foodItemAndQuantityList.add(foodItemAndQuantity);
                    // Set the Food Item List
                    userViewDto.setFoodItem(foodItemAndQuantityList);
                }

            }

            // Add the Order details to Dto list
            userViewDtos.add(userViewDto);
        }

        // return the list Of Orders with Food Items
        return userViewDtos;
    }

    // Get And Set the Address Details in Order View Dto
    private AddressDto setAddress(long orderId) throws CustomException {

        // Get the Order Details
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order is Not Available", HttpStatus.NOT_FOUND));

        // Check the Order is Available or not and Get the Address Details of this Order
        OrderAddressMap orderAddressMap = orderAddressMapRepository.findByOrder(order)
                .orElseThrow(() -> new CustomException("Order is Not Available", HttpStatus.NOT_FOUND));

        // Set the Address Details in Dto
        AddressDto address = new AddressDto();
        address.setDoorNo(orderAddressMap.getDoorNo());
        address.setStreet(orderAddressMap.getStreet());
        address.setCity(orderAddressMap.getCity());
        address.setState(orderAddressMap.getState());
        address.setPhoneNumber(orderAddressMap.getPhoneNumber());
        address.setOrderDate(orderAddressMap.getOrderDate());

        // return the address details
        return address;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderViewDto viewtheReceivedOrderByStaff(long userId, long orderId) throws CustomException {

        // Check Staff Access and Get the Order using Private Method
        Order order = staffAccessAndOrderChecking(userId, orderId);

        // Check Status of Order is Placed/Received or not
        if ((!order.getStatus().equals("Order Placed")) && (!order.getStatus().equals("Received Order by Staff"))) {
            throw new CustomException("Order is not Available", HttpStatus.NOT_FOUND);
        }

        // Get the List Of All Orders
        List<Order> orderList = orderRepository.findAll();

        // Get The Order Details
        OrderViewDto orderUserViewDto = new OrderViewDto();

        // Get the All Orders using Private method getOrders
        List<OrderViewDto> allOrders = getOrders(orderList);
        // for Each loop is used for get the Order by Id
        for (OrderViewDto orders : allOrders) {

            // Check the Order is Active or not And Get the Order by Order id
            if ((orders.getStatus().equals("Order Placed") || orders.getStatus().equals("Received Order by Staff"))
                    && (orders.getOrderId() == order.getOrderId())) {

                // Set the Orders details in dto to view staff
                orderUserViewDto.setOrderId(orders.getOrderId());
                orderUserViewDto.setFoodItem(orders.getFoodItem());
                // Set the Order Address Details using Private method getOrderId()
                orderUserViewDto.setAddress(setAddress(orders.getOrderId()));
                orderUserViewDto.setAmount(orders.getAmount());
                // Update the Status of the Order
                order.setStatus("Received Order by Staff");
                // Update the Status of the Order
                orderRepository.save(order);
                orderUserViewDto.setStatus(order.getStatus());

            }

        }

        // return the Order by Id
        return orderUserViewDto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTheOrderStatusToOrderPreparedByStaff(long userId, long orderId) throws CustomException {

        // Check Staff Access and Get the Order using Private Method
        Order order = staffAccessAndOrderChecking(userId, orderId);

        // Check Order is Received or not
        if (!order.getStatus().equals("Received Order by Staff")) {
            throw new CustomException("Order is Not Received", HttpStatus.NOT_FOUND);
        }

        // Update and Save the Order Status
        order.setStatus("Order Prepared");
        orderRepository.save(order);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTheOrderStatusToPendingDeliveryByStaff(long userId, long orderId) throws CustomException {

        // Check Staff Access and Get the Order using Private Method
        Order order = staffAccessAndOrderChecking(userId, orderId);

        // Check Order is Received or not
        if (!order.getStatus().equals("Order Prepared")) {
            throw new CustomException("Order is Not Received/Prepared", HttpStatus.NOT_FOUND);
        }

        // Update and Save the Order Status
        order.setStatus("Pending Deleivery");
        orderRepository.save(order);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTheOrderStatusToOrderDeliveredByStaff(long userId, long orderId) throws CustomException {

        // Check Staff Access and Get the Order using Private Method
        Order order = staffAccessAndOrderChecking(userId, orderId);

        // Check Order is Received or not
        if (!order.getStatus().equals("Pending Deleivery")) {
            throw new CustomException("Order is Not Received/Prepared/Packed", HttpStatus.NOT_FOUND);
        }

        // Update and Save the Order Status
        order.setStatus("Order Deliverd");
        orderRepository.save(order);

    }

    // This Method is used for Check the Staff Access and Order
    private Order staffAccessAndOrderChecking(long userId, long orderId) throws CustomException {
        // Check Valid User or Not
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User is Not Found", HttpStatus.NOT_FOUND));

        // Check Staff have Role or not
        UserRoleMap userRoleMap = userRoleMapRepository.findByUserId(user)
                .orElseThrow(() -> new CustomException("User Don't have any Role", HttpStatus.UNAUTHORIZED));

        // Staff Access Checking
        if (userRoleMap.getRoleId().getRoleId() != 2) {
            throw new CustomException("Not Access For This User", HttpStatus.UNAUTHORIZED);
        }

        // Check And Get the Order Details
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order is Not Available", HttpStatus.NOT_FOUND));

        return order;

    }

    // This Private Method is Checking Staff Access Only
    private void staffAccessChecking(long userId) throws CustomException {
        // Check Valid User or Not
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User is Not Found", HttpStatus.NOT_FOUND));

        // Check Staff have Role or not
        UserRoleMap userRoleMap = userRoleMapRepository.findByUserId(user)
                .orElseThrow(() -> new CustomException("User Don't have any Role", HttpStatus.UNAUTHORIZED));

        // Staff Access Checking
        if (userRoleMap.getRoleId().getRoleId() != 2) {
            throw new CustomException("Not Access For This User", HttpStatus.UNAUTHORIZED);
        }

    }

    // This Private Method is Checking User Access Only
    private User customerAccessChecking(long userId) throws CustomException {
        // Check Valid User or Not
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User is Not Found", HttpStatus.NOT_FOUND));

        // Check Staff have Role or not
        UserRoleMap userRoleMap = userRoleMapRepository.findByUserId(user)
                .orElseThrow(() -> new CustomException("User Don't have any Role", HttpStatus.UNAUTHORIZED));

        // Customer Access Checking
        if (userRoleMap.getRoleId().getRoleId() != 3) {
            throw new CustomException("Not Access For This User", HttpStatus.UNAUTHORIZED);
        }

        return user;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OrderViewDto> viewListOfCancelOrdersByStaff(long userId) throws CustomException {

        // Check Staff Access Using Private Method
        staffAccessChecking(userId);

        // Get List of Orders for this User
        List<Order> orderList = orderRepository.findAll();

        // Get the Cancel Orders List
        List<OrderViewDto> cancelOrders = new ArrayList<>();
        // Get the All Orders using Private method getOrders
        List<OrderViewDto> allOrders = getOrders(orderList);
        // for Each loop is used for get the Active Orders
        for (OrderViewDto orders : allOrders) {

            // Check the Order is Cancelled or not
            if (orders.getStatus().equals("Cancelled")) {
                // Set the Order Address Details using Private method getOrderId()
                orders.setAddress(setAddress(orders.getOrderId()));
                // Add the Cancel Orders
                cancelOrders.add(orders);

            }

        }

        // return the list Of Cancel Orders with Food Items
        return cancelOrders;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderViewDto viewCancelOrderByStaff(long userId, long orderId) throws CustomException {

        // Check Staff Access and Get the Order using Private Method
        Order order = staffAccessAndOrderChecking(userId, orderId);

        // Check Status of Order is Cancel or not
        if (!order.getStatus().equals("Cancelled")) {
            throw new CustomException("Order is not Available", HttpStatus.NOT_FOUND);
        }

        // Get the List Of All Orders
        List<Order> orderList = orderRepository.findAll();

        // Get The Cancel Order Details
        OrderViewDto orderUserViewDto = new OrderViewDto();

        // Get the All Orders using Private method getOrders
        List<OrderViewDto> allOrders = getOrders(orderList);
        // for Each loop is used for get the Order by Id
        for (OrderViewDto orders : allOrders) {

            // Check the Order is Cancelled or not And Get the Order by Order id
            if (orders.getStatus().equals("Cancelled") && (orders.getOrderId() == order.getOrderId())) {

                // Set the Orders details in dto to view staff
                orderUserViewDto.setOrderId(orders.getOrderId());
                orderUserViewDto.setFoodItem(orders.getFoodItem());
                // Set the Order Address Details using Private method getOrderId()
                orderUserViewDto.setAddress(setAddress(orders.getOrderId()));
                orderUserViewDto.setAmount(orders.getAmount());

                orderUserViewDto.setStatus(orders.getStatus());

            }

        }

        // return the Cancel Order by Id
        return orderUserViewDto;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OrderViewDto> viewListOfCompletedOrdersByStaff(long userId) throws CustomException {

        // Check Staff Access Using Private Method
        staffAccessChecking(userId);

        // Get List of Orders for this User
        List<Order> orderList = orderRepository.findAll();

        // Get the Completed Orders List
        List<OrderViewDto> completedOrders = new ArrayList<>();
        // Get the All Orders using Private method getOrders
        List<OrderViewDto> allOrders = getOrders(orderList);
        // for Each loop is used for get the Active Orders
        for (OrderViewDto orders : allOrders) {

            // Check the Order is Active or not
            if (orders.getStatus().equals("Order Deliverd")) {
                // Set the Order Address Details using Private method getOrderId()
                orders.setAddress(setAddress(orders.getOrderId()));
                // Add the Completed Orders
                completedOrders.add(orders);
            }

        }

        // return the list Of Completed Orders with Food Items
        return completedOrders;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderViewDto viewCompletedOrderByStaff(long userId, long orderId) throws CustomException {

        // Check Staff Access and Get the Order using Private Method
        Order order = staffAccessAndOrderChecking(userId, orderId);

        // Check Status of Order is Cancel or not
        if (!order.getStatus().equals("Order Deliverd")) {
            throw new CustomException("Order is not Available", HttpStatus.NOT_FOUND);
        }

        // Get the List Of All Orders
        List<Order> orderList = orderRepository.findAll();

        // Get The Completed Order Details
        OrderViewDto orderUserViewDto = new OrderViewDto();

        // Get the All Orders using Private method getOrders
        List<OrderViewDto> allOrders = getOrders(orderList);
        // for Each loop is used for get the Order by Id
        for (OrderViewDto orders : allOrders) {

            // Check the Order is Delivered or not And Get the Order by Order id
            if (orders.getStatus().equals("Order Deliverd") && (orders.getOrderId() == order.getOrderId())) {

                // Set the Orders details in dto to view staff
                orderUserViewDto.setOrderId(orders.getOrderId());
                orderUserViewDto.setFoodItem(orders.getFoodItem());
                // Set the Order Address Details using Private method getOrderId()
                orderUserViewDto.setAddress(setAddress(orders.getOrderId()));
                orderUserViewDto.setAmount(orders.getAmount());

                orderUserViewDto.setStatus(orders.getStatus());

            }

        }

        // return the Completed Order by Id
        return orderUserViewDto;
    }
}
