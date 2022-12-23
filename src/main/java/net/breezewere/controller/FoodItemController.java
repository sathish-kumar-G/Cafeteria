package net.breezewere.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.breezewere.entity.FoodItem;
import net.breezewere.exception.CustomException;
import net.breezewere.exception.ErrorResponse;
import net.breezewere.service.FoodItemService;

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
 * Food Item Controller is used for create, view, update and delete the Food
 * item. Auto wired this FoodItemController to FooditemService Interface.
 */

@RestController
@RequestMapping("/api")
@Slf4j
public class FoodItemController {

    @Autowired
    private FoodItemService foodItemService;

    // Create New Food Items
    @PostMapping("admin/{admin-id}/food-item")
    @Operation(method = "POST", summary = "Create the Food Item", description = "Create the Food Item")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FoodItem.class)))
    @Parameters(value = { @Parameter(allowEmptyValue = false, required = true, name = "userId",
            description = "Represents Users.", in = ParameterIn.PATH) })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success Payload",
                content = @Content(mediaType = "application.json", schema = @Schema(implementation = FoodItem.class),
                        examples = { @ExampleObject(name = "Success-200",
                                value = "{\"statusCode\":\"200\",\"message\":\"Success\",\"details\":\"Ok\"}") })),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application.json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-404",
                        value = "{\"statusCode\":\"404\",\"message\":\"Not Found\",\"details\":\"Food Item Is Not Available\"}") })),
        @ApiResponse(responseCode = "405", description = "Method Not Allowed", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-405",
                        value = "{\"statusCode\":\"405\",\"message\":\"Method Not Allowed\",\"details\":\"This Food Item Is Not Allowded\"}") })),
        @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(mediaType = "application.json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-409",
                        value = "{\"statusCode\":\"409\",\"message\":\"Conflict\",\"details\":\"Food Item is Already added\"}") })),
        @ApiResponse(responseCode = "415", description = "Unsupported Media Type", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-415",
                        value = "{\"statusCode\":\"415\",\"message\":\"Unsupported Media Type\",\"details\":\"Please Enter Correct Value\"}") })) })
    public FoodItem createFoodItem(@Valid @RequestBody FoodItem foodItem, @PathVariable(name = "admin-id") long userId)
            throws CustomException {
        log.info("Entering createFoodItem");
        FoodItem saveItem = foodItemService.createFoodItem(foodItem, userId);
        log.info("Leaving createFoodItem");
        return saveItem;
    }

    // View All Food Items
    @GetMapping("admin/{admin-id}/food-items")
    @Operation(method = "GET", summary = "Get the Food Items", description = "Get the Food Items")
    @Parameters(value = { @Parameter(allowEmptyValue = false, required = true, name = "userId",
            description = "Represents Users.", in = ParameterIn.PATH) })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success Payload",
                content = @Content(mediaType = "application.json", schema = @Schema(implementation = FoodItem.class),
                        examples = { @ExampleObject(name = "Success-200",
                                value = "{\"statusCode\":\"200\",\"message\":\"Success\",\"details\":\"Ok\"}") })),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application.json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-404",
                        value = "{\"statusCode\":\"404\",\"message\":\"Not Found\",\"details\":\"Food Item Is Not Available\"}") })),
        @ApiResponse(responseCode = "405", description = "Method Not Allowed", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-405",
                        value = "{\"statusCode\":\"405\",\"message\":\"Method Not Allowed\",\"details\":\"This Food item Is Not Allowded\"}") })),
        @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(mediaType = "application.json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-409",
                        value = "{\"statusCode\":\"409\",\"message\":\"Conflict\",\"details\":\"Already added\"}") })),
        @ApiResponse(responseCode = "415", description = "Unsupported Media Type", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-415",
                        value = "{\"statusCode\":\"415\",\"message\":\"Unsupported Media Type\",\"details\":\"Please Enter Correct Value\"}") })) })
    public List<FoodItem> viewAllFoodItem(@PathVariable(name = "admin-id") long userId) throws CustomException {
        log.info("Entering viewAllFoodItem");
        List<FoodItem> allFoodItem = foodItemService.viewAllFoodItem(userId);
        log.info("Leaving viewAllFoodItem");
        return allFoodItem;
    }

    // Update the Food Item By Id
    @PutMapping("admin/{admin-id}/food-item/{food-item-id}")
    @Operation(method = "PUT", summary = "Update the Food Item", description = "Update the Food Item")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FoodItem.class)))
    @Parameters(value = {
        @Parameter(allowEmptyValue = false, required = true, name = "foodItemId",
                description = "Represents Food Items.", in = ParameterIn.PATH),
        @Parameter(allowEmptyValue = false, required = true, name = "userId", description = "Represents Users.",
                in = ParameterIn.PATH) })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success Payload",
                content = @Content(mediaType = "application.json", schema = @Schema(implementation = FoodItem.class),
                        examples = { @ExampleObject(name = "Success-200",
                                value = "{\"statusCode\":\"200\",\"message\":\"Success\",\"details\":\"Ok\"}") })),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application.json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-404",
                        value = "{\"statusCode\":\"404\",\"message\":\"Not Found\",\"details\":\"Food Item Is Not Available\"}") })),
        @ApiResponse(responseCode = "405", description = "Method Not Allowed", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-405",
                        value = "{\"statusCode\":\"405\",\"message\":\"Method Not Allowed\",\"details\":\"This Food Is Not Allowded\"}") })),
        @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(mediaType = "application.json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-409",
                        value = "{\"statusCode\":\"409\",\"message\":\"Conflict\",\"details\":\"Already Added\"}") })),
        @ApiResponse(responseCode = "415", description = "Unsupported Media Type", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-415",
                        value = "{\"statusCode\":\"415\",\"message\":\"Unsupported Media Type\",\"details\":\"Please Enter Correct Value\"}") })) })
    public FoodItem updateFoodItemById(@Valid @RequestBody FoodItem foodItem,
            @PathVariable(name = "food-item-id") long foodItemId, @PathVariable(name = "admin-id") long userId)
            throws CustomException {
        log.info("Entering updateFoodItemById");
        FoodItem updateFoodItem = foodItemService.updateFoodItemById(foodItem, foodItemId, userId);
        log.info("Leaving updateFoodItemById");
        return updateFoodItem;

    }

    // Delete the Food Item
    @DeleteMapping("admin/{admin-id}/food-item/{food-item-id}")
    @Operation(method = "DELETE", summary = "Delete the Food Item.", description = "Delete the Food Item.")
    @Parameters(value = {
        @Parameter(allowEmptyValue = false, required = true, name = "foodItemId",
                description = "Represents Food Items.", in = ParameterIn.PATH),
        @Parameter(allowEmptyValue = false, required = true, name = "userId", description = "Represents Users.",
                in = ParameterIn.PATH) })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success Payload",
                content = @Content(mediaType = "application.json", schema = @Schema(implementation = FoodItem.class),
                        examples = { @ExampleObject(name = "Success-200",
                                value = "{\"statusCode\":\"200\",\"message\":\"Success\",\"details\":\"Ok\"}") })),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application.json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-404",
                        value = "{\"statusCode\":\"404\",\"message\":\"Not Found\",\"details\":\"Food Item Is Not Available\"}") })),
        @ApiResponse(responseCode = "405", description = "Method Not Allowed", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-405",
                        value = "{\"statusCode\":\"405\",\"message\":\"Method Not Allowed\",\"details\":\"Not Allowded\"}") })),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-400",
                        value = "{\"statusCode\":\"400\",\"message\":\"Bad Request\",\"details\":\"Please Enter Correct Value\"}") })),
        @ApiResponse(responseCode = "400", description = "Unsupported Media Type", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-415",
                        value = "{\"statusCode\":\"415\",\"message\":\"Unsupported Media Type\",\"details\":\"Please Enter Correct Value\"}") })) })
    public void deleteFoodItemById(@PathVariable(name = "food-item-id") long foodItemId,
            @PathVariable(name = "admin-id") long userId) throws CustomException {
        log.info("Entering deleteFoodItemById");
        foodItemService.deleteFoodItemById(foodItemId, userId);
        log.info("Leaving deleteFoodItemById");

    }

}
