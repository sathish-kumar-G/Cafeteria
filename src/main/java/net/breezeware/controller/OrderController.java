package net.breezeware.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.breezeware.dto.OrderDto;
import net.breezeware.dto.OrderUpdateDto;
import net.breezeware.dto.OrderViewDto;
import net.breezeware.entity.Order;
import net.breezeware.entity.OrderAddressMap;
import net.breezeware.exception.CustomException;
import net.breezeware.exception.ErrorResponse;
import net.breezeware.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
 * Order Controller is used to Manage the User Orders.
 * Autowired the Order Service.
 */

@RestController
@RequestMapping("/api")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Add the Food Items to the Order by User
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/user/{user-id}/order")
    @Operation(method = "POST", summary = "Create the Order", description = "Create the Order")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDto.class)))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success Payload",
                content = @Content(mediaType = "application.json", schema = @Schema(implementation = Order.class),
                        examples = { @ExampleObject(name = "Success-200",
                                value = "{\"statusCode\":\"200\",\"message\":\"Success\",\"details\":\"Ok\"}") })),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application.json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-404",
                        value = "{\"statusCode\":\"404\",\"message\":\"Not Found\",\"details\":\"Food Item Is Not Available\"}") })),
        @ApiResponse(responseCode = "405", description = "Method Not Allowed", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-405",
                        value = "{\"statusCode\":\"405\",\"message\":\"Method Not Allowed\",\"details\":\"This User Is Not Allowded\"}") })),
        @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(mediaType = "application.json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-409",
                        value = "{\"statusCode\":\"409\",\"message\":\"Conflict\",\"details\":\"User is Already Have Account\"}") })),
        @ApiResponse(responseCode = "415", description = "Unsupported Media Type", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-415",
                        value = "{\"statusCode\":\"415\",\"message\":\"Unsupported Media Type\",\"details\":\"Please Enter Correct Value\"}") })) })
    public Order addFoodItemsToOrder(@Valid @RequestBody OrderDto orderDto, @PathVariable(name = "user-id") long userId)
            throws CustomException {
        log.info("Entering addFoodItemsToOrder");
        Order saveOrder = orderService.addFoodItemsToOrder(orderDto, userId);
        log.info("Leaving addFoodItemsToOrder");
        return saveOrder;
    }

    // View All Orders by User
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user/{user-id}/orders")
    @Operation(method = "GET", summary = "Get the User Orders.", description = "Get the User Orders.")
    @Parameters(value = { @Parameter(allowEmptyValue = false, required = true, name = "userId",
            description = "Represents User Orders.", in = ParameterIn.PATH) })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success Payload",
                content = @Content(mediaType = "application.json",
                        schema = @Schema(implementation = OrderViewDto.class),
                        examples = { @ExampleObject(name = "Success-200",
                                value = "{\"statusCode\":\"200\",\"message\":\"Success\",\"details\":\"Ok\"}") })),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application.json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-404",
                        value = "{\"statusCode\":\"404\",\"message\":\"Not Found\",\"details\":\"User Is Not Available\"}") })),
        @ApiResponse(responseCode = "405", description = "Method Not Allowed", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-405",
                        value = "{\"statusCode\":\"405\",\"message\":\"Method Not Allowed\",\"details\":\"This User Is Not Allowded\"}") })),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-400",
                        value = "{\"statusCode\":\"400\",\"message\":\"Bad Request\",\"details\":\"Please Enter Correct Value\"}") })),
        @ApiResponse(responseCode = "415", description = "Unsupported Media Type", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-415",
                        value = "{\"statusCode\":\"415\",\"message\":\"Unsupported Media Type\",\"details\":\"Please Enter Correct Value\"}") })) })
    public List<OrderViewDto> viewAllUserOrder(@PathVariable(name = "user-id") long userId) throws CustomException {
        log.info("Entering viewAllUserOrder");
        List<OrderViewDto> viewAllOrder = orderService.viewUserOrder(userId);
        log.info("Leaving viewAllUserOrder");
        return viewAllOrder;
    }

    // Update the Order by User Using Order Id
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/user/{user-id}/order/{order-id}")
    @Operation(method = "PUT", summary = "Update the Food Items in Order",
            description = "Update the Food Items in Order")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderUpdateDto.class)))
    @Parameters(value = {
        @Parameter(allowEmptyValue = false, required = true, name = "userId", description = "Represents Users.",
                in = ParameterIn.PATH),
        @Parameter(allowEmptyValue = false, required = true, name = "orderId", description = "Represents Orders.",
                in = ParameterIn.PATH) })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success Payload",
                content = @Content(mediaType = "application.json",
                        schema = @Schema(implementation = OrderUpdateDto.class),
                        examples = { @ExampleObject(name = "Success-200",
                                value = "{\"statusCode\":\"200\",\"message\":\"Success\",\"details\":\"Ok\"}") })),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application.json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-404",
                        value = "{\"statusCode\":\"404\",\"message\":\"Not Found\",\"details\":\"User/Order/FoodItem Is Not Available\"}") })),
        @ApiResponse(responseCode = "405", description = "Method Not Allowed", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-405",
                        value = "{\"statusCode\":\"405\",\"message\":\"Method Not Allowed\",\"details\":\"This User Is Not Allowded\"}") })),
        @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(mediaType = "application.json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-409",
                        value = "{\"statusCode\":\"409\",\"message\":\"Conflict\",\"details\":\"User don't Have this Order\"}") })),
        @ApiResponse(responseCode = "415", description = "Unsupported Media Type", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-415",
                        value = "{\"statusCode\":\"415\",\"message\":\"Unsupported Media Type\",\"details\":\"Please Enter Correct Value\"}") })) })
    public OrderUpdateDto updateTheOrderByUserId(@RequestBody OrderUpdateDto orderUpdateDto,
            @PathVariable(name = "user-id") long userId, @PathVariable(name = "order-id") long orderId)
            throws CustomException {
        log.info("Entering updateTheOrderByUserId");
        OrderUpdateDto orderUpdate = orderService.updateTheOrderByUserId(orderUpdateDto, userId, orderId);
        log.info("Leaving updateTheOrderByUserId");
        return orderUpdate;
    }

    // Place an Order by User
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/user/{user-id}/place-order/{order-id}")
    @Operation(method = "POST", summary = "Place An Order", description = "Place An Order")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = OrderAddressMap.class)))
    @Parameters(value = {
        @Parameter(allowEmptyValue = false, required = true, name = "userId", description = "Represents Users.",
                in = ParameterIn.PATH),
        @Parameter(allowEmptyValue = false, required = true, name = "orderId", description = "Represents Orders.",
                in = ParameterIn.PATH) })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success Payload",
                content = @Content(mediaType = "application.json",
                        schema = @Schema(implementation = OrderAddressMap.class),
                        examples = { @ExampleObject(name = "Success-200",
                                value = "{\"statusCode\":\"200\",\"message\":\"Success\",\"details\":\"Ok\"}") })),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application.json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-404",
                        value = "{\"statusCode\":\"404\",\"message\":\"Not Found\",\"details\":\"User/Order Is Not Available\"}") })),
        @ApiResponse(responseCode = "405", description = "Method Not Allowed", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-405",
                        value = "{\"statusCode\":\"405\",\"message\":\"Method Not Allowed\",\"details\":\"This User Is Not Allowded\"}") })),
        @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(mediaType = "application.json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-409",
                        value = "{\"statusCode\":\"409\",\"message\":\"Conflict\",\"details\":\"Order is Already Have Ordered/Cancelled\"}") })),
        @ApiResponse(responseCode = "415", description = "Unsupported Media Type", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-415",
                        value = "{\"statusCode\":\"415\",\"message\":\"Unsupported Media Type\",\"details\":\"Please Enter Correct Value\"}") })) })
    public OrderAddressMap placeAnOrderByUser(@RequestBody OrderAddressMap orderAddressMap,
            @PathVariable(name = "user-id") long userId, @PathVariable(name = "order-id") long orderId)
            throws CustomException {

        log.info("Entering placeAnOrderByUser");
        OrderAddressMap placeOrder = orderService.placeAnOrderByUser(orderAddressMap, userId, orderId);
        log.info("Leaving placeAnOrderByUser");
        return placeOrder;
    }

    // Cancel the Order By User
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/user/{user-id}/cancel-order/{order-id}")
    @Operation(method = "DELETE", summary = "Cancel the Order.", description = "Cancel the Order.")
    @Parameters(value = {
        @Parameter(allowEmptyValue = false, required = true, name = "userId", description = "Represents Users.",
                in = ParameterIn.PATH),
        @Parameter(allowEmptyValue = false, required = true, name = "orderId", description = "Represents Orders.",
                in = ParameterIn.PATH) })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success Payload",
                content = @Content(mediaType = "application.json",
                        examples = { @ExampleObject(name = "Success-200",
                                value = "{\"statusCode\":\"200\",\"message\":\"Success\",\"details\":\"Ok\"}") })),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application.json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-404",
                        value = "{\"statusCode\":\"404\",\"message\":\"Not Found\",\"details\":\"User/Order Is Not Available\"}") })),
        @ApiResponse(responseCode = "405", description = "Method Not Allowed", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-405",
                        value = "{\"statusCode\":\"405\",\"message\":\"Method Not Allowed\",\"details\":\"This User Is Not Allowded\"}") })),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-400",
                        value = "{\"statusCode\":\"400\",\"message\":\"Bad Request\",\"details\":\"Please Enter Correct Value\"}") })),
        @ApiResponse(responseCode = "400", description = "Unsupported Media Type", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-415",
                        value = "{\"statusCode\":\"415\",\"message\":\"Unsupported Media Type\",\"details\":\"Please Enter Correct Value\"}") })) })
    public String cancelTheOrderByUser(@PathVariable(name = "user-id") long userId,
            @PathVariable(name = "order-id") long orderId) throws CustomException {
        log.info("Entering cancelTheOrderByUser");
        orderService.cancelTheOrderByUser(userId, orderId);
        log.info("Leaving cancelTheOrderByUser");
        return "Order is Cancelled";
    }

    // Get the All Active Orders by Staff
    @PreAuthorize("hasRole('ROLE_STAFF')")
    @GetMapping("/user/{staff-id}/active-orders")
    @Operation(method = "GET", summary = "Get the All Active Orders.", description = "Get the All Active Orders.")
    @Parameters(value = { @Parameter(allowEmptyValue = false, required = true, name = "userId",
            description = "Represents User Orders.", in = ParameterIn.PATH) })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success Payload",
                content = @Content(mediaType = "application.json",
                        schema = @Schema(implementation = OrderViewDto.class),
                        examples = { @ExampleObject(name = "Success-200",
                                value = "{\"statusCode\":\"200\",\"message\":\"Success\",\"details\":\"Ok\"}") })),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application.json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-404",
                        value = "{\"statusCode\":\"404\",\"message\":\"Not Found\",\"details\":\"Staff Is Not Available\"}") })),
        @ApiResponse(responseCode = "405", description = "Method Not Allowed", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-405",
                        value = "{\"statusCode\":\"405\",\"message\":\"Method Not Allowed\",\"details\":\"This Staff Is Not Allowded\"}") })),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-400",
                        value = "{\"statusCode\":\"400\",\"message\":\"Bad Request\",\"details\":\"Please Enter Correct Value\"}") })),
        @ApiResponse(responseCode = "415", description = "Unsupported Media Type", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-415",
                        value = "{\"statusCode\":\"415\",\"message\":\"Unsupported Media Type\",\"details\":\"Please Enter Correct Value\"}") })) })
    public List<OrderViewDto> getTheListOfActiveOrdersByStaff(@PathVariable(name = "staff-id") long userId)
            throws CustomException {
        log.info("Entering getTheListOfActiveOrdersByStaff");
        List<OrderViewDto> activeOrders = orderService.getTheListOfActiveOrdersByStaff(userId);
        log.info("Leaving getTheListOfActiveOrdersByStaff");
        return activeOrders;
    }

    // View the Received order by Staff
    @PreAuthorize("hasRole('ROLE_STAFF')")
    @GetMapping("/user/{staff-id}/received-order/{order-id}")
    @Operation(method = "GET", summary = "View the Received Order.", description = "View the Received Order.")
    @Parameters(value = {
        @Parameter(allowEmptyValue = false, required = true, name = "userId", description = "Represents Users.",
                in = ParameterIn.PATH),
        @Parameter(allowEmptyValue = false, required = true, name = "orderId", description = "Represents Orders.",
                in = ParameterIn.PATH) })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success Payload",
                content = @Content(mediaType = "application.json",
                        schema = @Schema(implementation = OrderViewDto.class),
                        examples = { @ExampleObject(name = "Success-200",
                                value = "{\"statusCode\":\"200\",\"message\":\"Success\",\"details\":\"Ok\"}") })),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application.json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-404",
                        value = "{\"statusCode\":\"404\",\"message\":\"Not Found\",\"details\":\"User/Order Is Not Available\"}") })),
        @ApiResponse(responseCode = "405", description = "Method Not Allowed", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-405",
                        value = "{\"statusCode\":\"405\",\"message\":\"Method Not Allowed\",\"details\":\"This User Is Not Allowded\"}") })),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-400",
                        value = "{\"statusCode\":\"400\",\"message\":\"Bad Request\",\"details\":\"Please Enter Correct Value\"}") })),
        @ApiResponse(responseCode = "400", description = "Unsupported Media Type", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-415",
                        value = "{\"statusCode\":\"415\",\"message\":\"Unsupported Media Type\",\"details\":\"Please Enter Correct Value\"}") })) })
    public OrderViewDto viewtheReceivedOrderByStaff(@PathVariable(name = "staff-id") long userId,
            @PathVariable(name = "order-id") long orderId) throws CustomException {
        log.info("Entering viewtheReceivedOrderByStaff");
        OrderViewDto receivedOrder = orderService.viewTheReceivedOrderByStaff(userId, orderId);
        log.info("Leaving viewtheReceivedOrderByStaff");
        return receivedOrder;
    }

    // Update the Order Status to Prepared Order by Staff
    @PreAuthorize("hasRole('ROLE_STAFF')")
    @PutMapping("/user/{staff-id}/order-prepared/{order-id}")
    @Operation(method = "PUT", summary = "Update Status to Prepared Order.",
            description = "Update Status to Prepared Order.")
    @Parameters(value = {
        @Parameter(allowEmptyValue = false, required = true, name = "userId", description = "Represents Users.",
                in = ParameterIn.PATH),
        @Parameter(allowEmptyValue = false, required = true, name = "orderId", description = "Represents Orders.",
                in = ParameterIn.PATH) })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success Payload",
                content = @Content(mediaType = "application.json",
                        examples = { @ExampleObject(name = "Success-200",
                                value = "{\"statusCode\":\"200\",\"message\":\"Success\",\"details\":\"Ok\"}") })),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application.json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-404",
                        value = "{\"statusCode\":\"404\",\"message\":\"Not Found\",\"details\":\"User/Order Is Not Available\"}") })),
        @ApiResponse(responseCode = "405", description = "Method Not Allowed", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-405",
                        value = "{\"statusCode\":\"405\",\"message\":\"Method Not Allowed\",\"details\":\"This User Is Not Allowded\"}") })),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-400",
                        value = "{\"statusCode\":\"400\",\"message\":\"Bad Request\",\"details\":\"Please Enter Correct Value\"}") })),
        @ApiResponse(responseCode = "400", description = "Unsupported Media Type", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-415",
                        value = "{\"statusCode\":\"415\",\"message\":\"Unsupported Media Type\",\"details\":\"Please Enter Correct Value\"}") })) })
    public String updateTheOrderStatusToOrderPreparedByStaff(@PathVariable(name = "staff-id") long userId,
            @PathVariable(name = "order-id") long orderId) throws CustomException {
        log.info("Entering updateTheOrderStatusToOrderPreparedByStaff");
        orderService.updateTheOrderStatusToOrderPreparedByStaff(userId, orderId);
        log.info("Leaving updateTheOrderStatusToOrderPreparedByStaff");
        return "Ordered Prepared";
    }

    // Update the Order Status to Pending Delivery by Staff
    @PreAuthorize("hasRole('ROLE_STAFF')")
    @PutMapping("/user/{staff-id}/pending-delivery/{order-id}")
    @Operation(method = "PUT", summary = "Update Status to Pending Delivery.",
            description = "Update Status to Pending Delivery.")
    @Parameters(value = {
        @Parameter(allowEmptyValue = false, required = true, name = "userId", description = "Represents Users.",
                in = ParameterIn.PATH),
        @Parameter(allowEmptyValue = false, required = true, name = "orderId", description = "Represents Orders.",
                in = ParameterIn.PATH) })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success Payload",
                content = @Content(mediaType = "application.json",
                        examples = { @ExampleObject(name = "Success-200",
                                value = "{\"statusCode\":\"200\",\"message\":\"Success\",\"details\":\"Ok\"}") })),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application.json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-404",
                        value = "{\"statusCode\":\"404\",\"message\":\"Not Found\",\"details\":\"User/Order Is Not Available\"}") })),
        @ApiResponse(responseCode = "405", description = "Method Not Allowed", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-405",
                        value = "{\"statusCode\":\"405\",\"message\":\"Method Not Allowed\",\"details\":\"This User Is Not Allowded\"}") })),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-400",
                        value = "{\"statusCode\":\"400\",\"message\":\"Bad Request\",\"details\":\"Please Enter Correct Value\"}") })),
        @ApiResponse(responseCode = "400", description = "Unsupported Media Type", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-415",
                        value = "{\"statusCode\":\"415\",\"message\":\"Unsupported Media Type\",\"details\":\"Please Enter Correct Value\"}") })) })
    public String updateTheOrderStatusToPendingDeliveryByStaff(@PathVariable(name = "staff-id") long userId,
            @PathVariable(name = "order-id") long orderId) throws CustomException {
        log.info("Entering updateTheOrderStatusToPendingDeliveryByStaff");
        orderService.updateTheOrderStatusToPendingDeliveryByStaff(userId, orderId);
        log.info("Leaving updateTheOrderStatusToPendingDeliveryByStaff");
        return "Pending Delivery";
    }

    // Update the Order Status to Order Delivered by Staff
    @PreAuthorize("hasRole('ROLE_STAFF')")
    @PutMapping("/user/{staff-id}/order-delivered/{order-id}")
    @Operation(method = "PUT", summary = "Update Status to Order Delivered.",
            description = "Update Status to Order Delivered.")
    @Parameters(value = {
        @Parameter(allowEmptyValue = false, required = true, name = "userId", description = "Represents Users.",
                in = ParameterIn.PATH),
        @Parameter(allowEmptyValue = false, required = true, name = "orderId", description = "Represents Orders.",
                in = ParameterIn.PATH) })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success Payload",
                content = @Content(mediaType = "application.json",
                        examples = { @ExampleObject(name = "Success-200",
                                value = "{\"statusCode\":\"200\",\"message\":\"Success\",\"details\":\"Ok\"}") })),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application.json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-404",
                        value = "{\"statusCode\":\"404\",\"message\":\"Not Found\",\"details\":\"User/Order Is Not Available\"}") })),
        @ApiResponse(responseCode = "405", description = "Method Not Allowed", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-405",
                        value = "{\"statusCode\":\"405\",\"message\":\"Method Not Allowed\",\"details\":\"This User Is Not Allowded\"}") })),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-400",
                        value = "{\"statusCode\":\"400\",\"message\":\"Bad Request\",\"details\":\"Please Enter Correct Value\"}") })),
        @ApiResponse(responseCode = "400", description = "Unsupported Media Type", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-415",
                        value = "{\"statusCode\":\"415\",\"message\":\"Unsupported Media Type\",\"details\":\"Please Enter Correct Value\"}") })) })
    public String updateTheOrderStatusToOrderDeliveredByStaff(@PathVariable(name = "staff-id") long userId,
            @PathVariable(name = "order-id") long orderId) throws CustomException {
        log.info("Entering updateTheOrderStatusToOrderDeliveredByStaff");
        orderService.updateTheOrderStatusToOrderDeliveredByStaff(userId, orderId);
        log.info("Leaving updateTheOrderStatusToOrderDeliveredByStaff");
        return "Order Delivered";
    }

    // View List of Cancel Orders By Staff
    @PreAuthorize("hasRole('ROLE_STAFF')")
    @GetMapping("/user/{staff-id}/cancel-orders")
    @Operation(method = "GET", summary = "Get the All Cancel Orders.", description = "Get the All Cancel Orders.")
    @Parameters(value = { @Parameter(allowEmptyValue = false, required = true, name = "userId",
            description = "Represents User Orders.", in = ParameterIn.PATH) })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success Payload",
                content = @Content(mediaType = "application.json",
                        schema = @Schema(implementation = OrderViewDto.class),
                        examples = { @ExampleObject(name = "Success-200",
                                value = "{\"statusCode\":\"200\",\"message\":\"Success\",\"details\":\"Ok\"}") })),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application.json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-404",
                        value = "{\"statusCode\":\"404\",\"message\":\"Not Found\",\"details\":\"Staff Is Not Available\"}") })),
        @ApiResponse(responseCode = "405", description = "Method Not Allowed", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-405",
                        value = "{\"statusCode\":\"405\",\"message\":\"Method Not Allowed\",\"details\":\"This User Is Not Allowded\"}") })),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-400",
                        value = "{\"statusCode\":\"400\",\"message\":\"Bad Request\",\"details\":\"Please Enter Correct Value\"}") })),
        @ApiResponse(responseCode = "415", description = "Unsupported Media Type", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-415",
                        value = "{\"statusCode\":\"415\",\"message\":\"Unsupported Media Type\",\"details\":\"Please Enter Correct Value\"}") })) })
    public List<OrderViewDto> viewListOfCancelOrdersByStaff(@PathVariable(name = "staff-id") long userId)
            throws CustomException {
        log.info("Entering viewListOfCancelOrdersByStaff");
        List<OrderViewDto> cancelOrders = orderService.viewListOfCancelOrdersByStaff(userId);
        log.info("Leaving viewListOfCancelOrdersByStaff");
        return cancelOrders;
    }

    // Get the Cancel Order by Staff
    @PreAuthorize("hasRole('ROLE_STAFF')")
    @GetMapping("/user/{staff-id}/cancel-order/{order-id}")
    @Operation(method = "GET", summary = "View the Cancelled Order.", description = "View the Cancelled Order.")
    @Parameters(value = {
        @Parameter(allowEmptyValue = false, required = true, name = "userId", description = "Represents Users.",
                in = ParameterIn.PATH),
        @Parameter(allowEmptyValue = false, required = true, name = "orderId", description = "Represents Orders.",
                in = ParameterIn.PATH) })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success Payload",
                content = @Content(mediaType = "application.json",
                        schema = @Schema(implementation = OrderViewDto.class),
                        examples = { @ExampleObject(name = "Success-200",
                                value = "{\"statusCode\":\"200\",\"message\":\"Success\",\"details\":\"Ok\"}") })),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application.json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-404",
                        value = "{\"statusCode\":\"404\",\"message\":\"Not Found\",\"details\":\"User/Order Is Not Available\"}") })),
        @ApiResponse(responseCode = "405", description = "Method Not Allowed", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-405",
                        value = "{\"statusCode\":\"405\",\"message\":\"Method Not Allowed\",\"details\":\"This User Is Not Allowded\"}") })),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-400",
                        value = "{\"statusCode\":\"400\",\"message\":\"Bad Request\",\"details\":\"Please Enter Correct Value\"}") })),
        @ApiResponse(responseCode = "400", description = "Unsupported Media Type", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-415",
                        value = "{\"statusCode\":\"415\",\"message\":\"Unsupported Media Type\",\"details\":\"Please Enter Correct Value\"}") })) })
    public OrderViewDto viewCancelOrderByStaff(@PathVariable(name = "staff-id") long userId,
            @PathVariable(name = "order-id") long orderId) throws CustomException {
        log.info("Entering viewCancelOrderByStaff");
        OrderViewDto cancelOrder = orderService.viewCancelOrderByStaff(userId, orderId);
        log.info("Leaving viewCancelOrderByStaff");
        return cancelOrder;
    }

    // View List of Completed Orders By Staff
    @PreAuthorize("hasRole('ROLE_STAFF')")
    @GetMapping("/user/{staff-id}/completed-orders")
    @Operation(method = "GET", summary = "Get the All Completed Orders.", description = "Get the All Completed Orders.")
    @Parameters(value = { @Parameter(allowEmptyValue = false, required = true, name = "userId",
            description = "Represents Users.", in = ParameterIn.PATH) })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success Payload",
                content = @Content(mediaType = "application.json",
                        schema = @Schema(implementation = OrderViewDto.class),
                        examples = { @ExampleObject(name = "Success-200",
                                value = "{\"statusCode\":\"200\",\"message\":\"Success\",\"details\":\"Ok\"}") })),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application.json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-404",
                        value = "{\"statusCode\":\"404\",\"message\":\"Not Found\",\"details\":\"Staff Is Not Available\"}") })),
        @ApiResponse(responseCode = "405", description = "Method Not Allowed", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-405",
                        value = "{\"statusCode\":\"405\",\"message\":\"Method Not Allowed\",\"details\":\"This User Is Not Allowded\"}") })),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-400",
                        value = "{\"statusCode\":\"400\",\"message\":\"Bad Request\",\"details\":\"Please Enter Correct Value\"}") })),
        @ApiResponse(responseCode = "415", description = "Unsupported Media Type", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-415",
                        value = "{\"statusCode\":\"415\",\"message\":\"Unsupported Media Type\",\"details\":\"Please Enter Correct Value\"}") })) })
    public List<OrderViewDto> viewListOfCompletedOrdersByStaff(@PathVariable(name = "staff-id") long userId)
            throws CustomException {
        log.info("Entering viewListOfCompletedOrdersByStaff");
        List<OrderViewDto> completedOrders = orderService.viewListOfCompletedOrdersByStaff(userId);
        log.info("Leaving viewListOfCompletedOrdersByStaff");
        return completedOrders;
    }

    // Get the Completed Order by Staff
    @PreAuthorize("hasRole('ROLE_STAFF')")
    @GetMapping("/user/{staff-id}/completed-order/{order-id}")
    @Operation(method = "GET", summary = "View the Completed Order.", description = "View the Completed Order.")
    @Parameters(value = {
        @Parameter(allowEmptyValue = false, required = true, name = "userId", description = "Represents Users.",
                in = ParameterIn.PATH),
        @Parameter(allowEmptyValue = false, required = true, name = "orderId", description = "Represents Orders.",
                in = ParameterIn.PATH) })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success Payload",
                content = @Content(mediaType = "application.json",
                        schema = @Schema(implementation = OrderViewDto.class),
                        examples = { @ExampleObject(name = "Success-200",
                                value = "{\"statusCode\":\"200\",\"message\":\"Success\",\"details\":\"Ok\"}") })),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application.json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-404",
                        value = "{\"statusCode\":\"404\",\"message\":\"Not Found\",\"details\":\"User/Order Is Not Available\"}") })),
        @ApiResponse(responseCode = "405", description = "Method Not Allowed", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-405",
                        value = "{\"statusCode\":\"405\",\"message\":\"Method Not Allowed\",\"details\":\"This User Is Not Allowded\"}") })),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-400",
                        value = "{\"statusCode\":\"400\",\"message\":\"Bad Request\",\"details\":\"Please Enter Correct Value\"}") })),
        @ApiResponse(responseCode = "400", description = "Unsupported Media Type", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-415",
                        value = "{\"statusCode\":\"415\",\"message\":\"Unsupported Media Type\",\"details\":\"Please Enter Correct Value\"}") })) })
    public OrderViewDto viewCompletedOrderByStaff(@PathVariable(name = "staff-id") long userId,
            @PathVariable(name = "order-id") long orderId) throws CustomException {
        log.info("Entering viewCompletedOrderByStaff");
        OrderViewDto completedOrder = orderService.viewCompletedOrderByStaff(userId, orderId);
        log.info("Leaving viewCompletedOrderByStaff");
        return completedOrder;
    }

}
