package net.breezeware.service;

import java.util.List;

import javax.validation.Valid;

import net.breezeware.dto.OrderDto;
import net.breezeware.dto.OrderUpdateDto;
import net.breezeware.dto.OrderViewDto;
import net.breezeware.dynamo.utils.exception.DynamoException;
import net.breezeware.entity.Order;
import net.breezeware.entity.OrderAddressMap;
import net.breezeware.exception.CustomException;

/**
 * OrderServiceImpl implements OrderService Interface.
 */
public interface OrderService {

    /**
     * User Add Food items to the Order.
     * @param  orderDto        this detail is used for add Food Items to the Order
     * @param  userId          this id is used to find the user.
     * @return                 Order details.
     * @throws DynamoException if Food item or user is not available.
     */
    Order addFoodItemsToOrder(@Valid OrderDto orderDto, long userId) throws DynamoException;

    /**
     * View all the Order details for this User.
     * @param  userId          this id is used to find the user.
     * @return                 all order details.
     * @throws DynamoException if User is not Found.
     */
    List<OrderViewDto> viewUserOrder(long userId) throws DynamoException;

    /**
     * Update the Order By User using Order id.
     * @param  orderUpdateDto  Update the Food Items in the Order.
     * @param  userId          this id is used to find the user.
     * @param  orderId         this id is used to find the order details.
     * @return     Updated order details.
     * @throws DynamoException if Order, Food item or user is not available.
     */
    OrderUpdateDto updateTheOrderByUserId(OrderUpdateDto orderUpdateDto, long userId, long orderId)
            throws DynamoException;

    /**
     * Place an Order by User.
     * @param  orderAddressMap Sets the Address Details in the Order.
     * @param  userId          this id is used to find the user.
     * @param  orderId         this id is used to find the order details.
     * @return                 Order with Address Details.
     * @throws DynamoException if Order or user is not available.
     */
    OrderAddressMap placeAnOrderByUser(OrderAddressMap orderAddressMap, long userId, long orderId)
            throws DynamoException;

    /**
     * Cancel the Placed order by User.
     * @param  userId          this id is used to find the user.
     * @param  orderId         this id is used to find the order details.
     * @throws DynamoException if Order or user is not available.
     */
    void cancelTheOrderByUser(long userId, long orderId) throws DynamoException;

    /**
     * Gets the List of Active placed orders by Staff.
     * @param  userId         this id is used to find the staff.
     * @return                List of Active placed orders.
     * @throws DynamoException if Staff is not Found.
     */
    List<OrderViewDto> getTheListOfActiveOrdersByStaff(long userId) throws DynamoException;

    /**
     * Get the Received Order By Staff.
     * @param  userId          this id is used to find the staff.
     * @param  orderId         this id is used to find the order details.
     * @return                 received order.
     * @throws DynamoException if Order or staff is not available.
     */
    OrderViewDto viewTheReceivedOrderByStaff(long userId, long orderId) throws DynamoException;

    /**
     * Update the Order Status to Prepared Order by Staff.
     * @param  userId          this id is used to find the staff.
     * @param  orderId         this id is used to find the order details.
     * @throws DynamoException if Order or staff is not available.
     */
    void updateTheOrderStatusToOrderPreparedByStaff(long userId, long orderId) throws DynamoException;

    /**
     * Update the Order Status to Delivery Pending by Staff
     * @param  userId          this id is used to find the staff.
     * @param  orderId         this id is used to find the order details.
     * @throws DynamoException if Order or staff is not available.
     */
    void updateTheOrderStatusToPendingDeliveryByStaff(long userId, long orderId) throws DynamoException;

    /**
     * Update the Order Status to Order Delivered by Staff.
     * @param  userId          this id is used to find the staff.
     * @param  orderId         this id is used to find the order details.
     * @throws DynamoException if Order or staff is not available.
     */
    void updateTheOrderStatusToOrderDeliveredByStaff(long userId, long orderId) throws DynamoException;

    /**
     * Gets List of Cancelled Order by Staff.
     * @param  userId          this id is used to find the staff.
     * @return                 List of Cancelled Orders.
     * @throws DynamoException if staff is not Found.
     */
    List<OrderViewDto> viewListOfCancelOrdersByStaff(long userId) throws DynamoException;

    /**
     * Get the Cancel Order by Staff using Order id.
     * @param  userId          this id is used to find the staff.
     * @param  orderId         this id is used to find the order details.
     * @return                 Cancel Order.
     * @throws DynamoException if Order or staff is not available.
     */
    OrderViewDto viewCancelOrderByStaff(long userId, long orderId) throws DynamoException;

    /**
     * Gets List of Completed Orders by Staff.
     * @param  userId          this id is used to find the staff.
     * @return                 List of Completed Orders.
     * @throws DynamoException if staff is not available.
     */
    List<OrderViewDto> viewListOfCompletedOrdersByStaff(long userId) throws DynamoException;

    /**
     * Get the Completed Order by Staff using Order id.
     * @param  userId         this id is used to find the staff.
     * @param  orderId        this id is used to find the order details.
     * @return                 Completed Order.
     * @throws DynamoException if Order or staff is not available.
     */
    OrderViewDto viewCompletedOrderByStaff(long userId, long orderId) throws DynamoException;

}
