package net.breezewere.service;

import java.util.List;

import javax.validation.Valid;

import net.breezewere.dto.OrderDto;
import net.breezewere.dto.OrderUpdateDto;
import net.breezewere.dto.OrderViewDto;
import net.breezewere.entity.Order;
import net.breezewere.entity.OrderAddressMap;
import net.breezewere.exception.CustomException;

public interface OrderService {

    /**
     * User Add Food items to the Order
     * @param  orderDto        Dto class used for add Food Items to the Order
     * @return                 Order
     * @throws CustomException throws Food item is not available
     */
    Order addFoodItemsToOrder(@Valid OrderDto orderDto, long userId) throws CustomException;

    /**
     * View the All User Order for this User
     * @param  userId          to get All Orders for this user id
     * @return
     * @throws CustomException if User is not Found
     */
    List<OrderViewDto> viewUserOrder(long userId) throws CustomException;

    /**
     * Update the Order By User Id
     * @param  orderUpdateDto  Set and Get the Updated Food Items in the Order
     * @param  userId          find the User
     * @param  orderId         find the Order
     * @return
     * @throws CustomException throws if user/order/foodItem is not Available
     */
    OrderUpdateDto updateTheOrderByUserId(OrderUpdateDto orderUpdateDto, long userId, long orderId)
            throws CustomException;

    /**
     * Place an Order by User
     * @param  orderAddressMap Store the data in Order Address Map table
     * @param  userId          find the User
     * @param  orderId         find the Order
     * @return
     * @throws CustomException throw if User/Order not Found
     */
    OrderAddressMap placeAnOrderByUser(OrderAddressMap orderAddressMap, long userId, long orderId)
            throws CustomException;

    /**
     * Cancel the Order by User
     * @param  userId          find the User
     * @param  orderId         find the Order
     * @throws CustomException throw if User/Order not Found
     */
    void cancelTheOrderByUser(long userId, long orderId) throws CustomException;

    /**
     * Get the List of Active Orders by Staff
     * @param  userId          find the Staff
     * @return
     * @throws CustomException throw if Staff is not Found
     */
    List<OrderViewDto> getTheListOfActiveOrdersByStaff(long userId) throws CustomException;

    /**
     * View the Received Order By Staff
     * @param  userId          find the Staff
     * @param  orderId         find the Order
     * @return
     * @throws CustomException if User/Order not Found
     */
    OrderViewDto viewtheReceivedOrderByStaff(long userId, long orderId) throws CustomException;

    /**
     * Update the Order Status to Prepared Order by Staff
     * @param  userId          find the Staff
     * @param  orderId         find the Order
     * @throws CustomException if User/Order not Found
     */
    void updateTheOrderStatusToOrderPreparedByStaff(long userId, long orderId) throws CustomException;

    /**
     * Update the Order Status to Delivery Pending by Staff
     * @param  userId          find the Staff
     * @param  orderId         find the Order
     * @throws CustomException if User/Order not Found
     */
    void updateTheOrderStatusToPendingDeliveryByStaff(long userId, long orderId) throws CustomException;

    /**
     * Update the Order Status to Order Delivered by Staff
     * @param  userId          find the Staff
     * @param  orderId         find the Order
     * @throws CustomException if User/Order not Found
     */
    void updateTheOrderStatusToOrderDeliveredByStaff(long userId, long orderId) throws CustomException;

    /**
     * View List of Cancelled Order by Staff
     * @param  userId          find the Staff
     * @return
     * @throws CustomException if User/Order not Found
     */
    List<OrderViewDto> viewListOfCancelOrdersByStaff(long userId) throws CustomException;

    /**
     * View the Cancel Order by Staff using Order Id
     * @param  userId          find the Staff
     * @param  orderId         find the Order
     * @return                 Cancel Order
     * @throws CustomException if User/Order not Found
     */
    OrderViewDto viewCancelOrderByStaff(long userId, long orderId) throws CustomException;

    /**
     * View List of Completed Orders by Staff
     * @param  userId          find the Staff
     * @return                 Completed Orders
     * @throws CustomException if User/Order not Found
     */
    List<OrderViewDto> viewListOfCompletedOrdersByStaff(long userId) throws CustomException;

    /**
     * View the Completed Order by Staff using Order Id
     * @param  userId          find the Staff
     * @param  orderId         find the Order
     * @return                 Completed Order
     * @throws CustomException if User/Order not Found
     */
    OrderViewDto viewCompletedOrderByStaff(long userId, long orderId) throws CustomException;

}
