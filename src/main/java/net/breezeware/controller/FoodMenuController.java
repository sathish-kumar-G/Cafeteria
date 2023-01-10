package net.breezeware.controller;

import java.util.List;

import javax.validation.Valid;

import net.breezeware.dynamo.utils.exception.DynamoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import net.breezeware.dto.FoodMenuDto;
import net.breezeware.dto.FoodMenuUpdateDto;
import net.breezeware.dto.FoodMenuViewDto;
import net.breezeware.dto.FoodMenuViewUserDto;
import net.breezeware.entity.FoodMenu;
import net.breezeware.exception.CustomException;
import net.breezeware.exception.ErrorResponse;
import net.breezeware.service.FoodMenuService;

import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * Food Menu Controller is used for create, view, update and delete the Food Menu with Food
 * items. Auto wired this FoodMenuController to FoodMenuService Interface.
 */

@RestController
@RequestMapping("/api")
@Slf4j
public class FoodMenuController {

    @Autowired
    private FoodMenuService foodMenuService;

    // Create the new Food Menu with Food Items by Admin
    /**
     * Create the new Food Menu with Food Items by Admin.
     * @param  foodMenu        Food Menu with Food Items details.
     * @param  userId          this id is used to find the admin.
     * @return                 Created new Food menu with Food Items.
     * @throws DynamoException if food menu details null or conflict.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/user/{admin-id}/food-menu")
    @Operation(method = "POST", summary = "Create the Food Menu", description = "Create the Food Menu")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FoodMenuDto.class)))
    @Parameters(value = { @Parameter(allowEmptyValue = false, required = true, name = "userId",
            description = "Represents Users.", in = ParameterIn.PATH) })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success Payload",
                content = @Content(mediaType = "application.json", schema = @Schema(implementation = FoodMenu.class),
                        examples = { @ExampleObject(name = "Success-200",
                                value = "{\"statusCode\":\"200\",\"message\":\"Success\",\"details\":\"Ok\"}") })),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application.json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-404",
                        value = "{\"statusCode\":\"404\",\"message\":\"Not Found\",\"details\":\"Food Menu Is Not Available\"}") })),
        @ApiResponse(responseCode = "405", description = "Method Not Allowed", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-405",
                        value = "{\"statusCode\":\"405\",\"message\":\"Method Not Allowed\",\"details\":\"This Food menu Is Not Allowded\"}") })),
        @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(mediaType = "application.json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-409",
                        value = "{\"statusCode\":\"409\",\"message\":\"Conflict\",\"details\":\"Food Menu is Already added\"}") })),
        @ApiResponse(responseCode = "415", description = "Unsupported Media Type", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-415",
                        value = "{\"statusCode\":\"415\",\"message\":\"Unsupported Media Type\",\"details\":\"Please Enter Correct Value\"}") })) })
    public FoodMenu createFoodMenu(@Valid @RequestBody FoodMenuDto foodMenu,
            @PathVariable(name = "admin-id") long userId) throws DynamoException {
        log.info("Entering createFoodMenu {}", foodMenu);
        FoodMenu saveFoodMenu = foodMenuService.createFoodMenu(foodMenu, userId);
        log.info("Leaving createFoodMenu");
        return saveFoodMenu;

    }

    // View Food Menu By id
    /**
     * Gets the Food Menu with Food items by Admin.
     * @param  foodMenuId      this id is used to fins the foodMenu
     * @param  userId          this id is used to find the admin.
     * @return                 the Food Menu with Food Items
     * @throws DynamoException if foodMenu or Admin is not found.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/user/{admin-id}/food-menu/{food-menu-id}")
    @Operation(method = "GET", summary = "Get the Food Menu.", description = "Get the Food Menu.")
    @Parameters(value = {
        @Parameter(allowEmptyValue = false, required = true, name = "foodMenuId", description = "Represents Food Menu.",
                in = ParameterIn.PATH),
        @Parameter(allowEmptyValue = false, required = true, name = "userId", description = "Represents Users.",
                in = ParameterIn.PATH) })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success Payload",
                content = @Content(mediaType = "application.json",
                        schema = @Schema(implementation = FoodMenuViewDto.class),
                        examples = { @ExampleObject(name = "Success-200",
                                value = "{\"statusCode\":\"200\",\"message\":\"Success\",\"details\":\"Ok\"}") })),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application.json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-404",
                        value = "{\"statusCode\":\"404\",\"message\":\"Not Found\",\"details\":\"Food Menu Is Not Available\"}") })),
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
    public FoodMenuViewDto viewFoodMenuById(@PathVariable(name = "food-menu-id") long foodMenuId,
            @PathVariable(name = "admin-id") long userId) throws DynamoException {
        log.info("Entering View {}", foodMenuId);
        FoodMenuViewDto viewFoodMenu = foodMenuService.getFoodMenuById(foodMenuId, userId);
        log.info("Releaving viewAllFoodMenu {}", foodMenuId);
        return viewFoodMenu;
    }

    // Update The Food Menu
    /**
     * Update the Food Menu with Food Items by Admin.
     * @param  foodMenuUpdateDto Update the Food Menu and Food Items data.
     * @param  foodMenuId        this id is used to fins the foodMenu.
     * @param  userId            this id is used to find the admin.
     * @return                   Updated Food menu with Food Items.
     * @throws DynamoException   if foodMenu or Admin is not found.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/user/{admin-id}/food-menu/{food-menu-id}")
    @Operation(method = "PUT", summary = "Update the Food Menu", description = "Update the Food Menu")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = FoodMenuUpdateDto.class)))
    @Parameters(value = {
        @Parameter(allowEmptyValue = false, required = true, name = "foodMenuId", description = "Represents Food Menu.",
                in = ParameterIn.PATH),
        @Parameter(allowEmptyValue = false, required = true, name = "userId", description = "Represents Users.",
                in = ParameterIn.PATH) })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success Payload",
                content = @Content(mediaType = "application.json",
                        schema = @Schema(implementation = FoodMenuUpdateDto.class),
                        examples = { @ExampleObject(name = "Success-200",
                                value = "{\"statusCode\":\"200\",\"message\":\"Success\",\"details\":\"Ok\"}") })),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application.json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-404",
                        value = "{\"statusCode\":\"404\",\"message\":\"Not Found\",\"details\":\"Food Menu Is Not Available\"}") })),
        @ApiResponse(responseCode = "405", description = "Method Not Allowed", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-405",
                        value = "{\"statusCode\":\"405\",\"message\":\"Method Not Allowed\",\"details\":\"This User Is Not Allowded\"}") })),
        @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(mediaType = "application.json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-409",
                        value = "{\"statusCode\":\"409\",\"message\":\"Conflict\",\"details\":\"Food Menu is Already Created\"}") })),
        @ApiResponse(responseCode = "415", description = "Unsupported Media Type", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-415",
                        value = "{\"statusCode\":\"415\",\"message\":\"Unsupported Media Type\",\"details\":\"Please Enter Correct Value\"}") })) })
    public FoodMenuUpdateDto updateFoodMenuById(@RequestBody FoodMenuUpdateDto foodMenuUpdateDto,
            @PathVariable(name = "food-menu-id") long foodMenuId, @PathVariable(name = "admin-id") long userId)
            throws DynamoException {
        log.info("Entering updateFoodMenuById");
        FoodMenuUpdateDto updateFoodMenu = foodMenuService.updateFoodMenu(foodMenuUpdateDto, foodMenuId, userId);
        log.info("Releaving updateFoodMenuById");

        return updateFoodMenu;
    }

    // Delete the Food Menu
    /**
     * Delete the Food Menu with Food Items by Admin.
     * @param  foodMenuId      this id is used to fins the foodMenu.
     * @param  userId          this id is used to find the admin.
     * @throws DynamoException if foodMenu or Admin is not found.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/user/{admin-id}/food-menu/{food-menu-id}")
    @Operation(method = "DELETE", summary = "Delete the Food Menu.", description = "Delete the Food Menu.")
    @Parameters(value = {
        @Parameter(allowEmptyValue = false, required = true, name = "foodMenuId", description = "Represents Food Menu.",
                in = ParameterIn.PATH),
        @Parameter(allowEmptyValue = false, required = true, name = "userId", description = "Represents Users.",
                in = ParameterIn.PATH) })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success Payload",
                content = @Content(mediaType = "application.json", schema = @Schema(implementation = FoodMenu.class),
                        examples = { @ExampleObject(name = "Success-200",
                                value = "{\"statusCode\":\"200\",\"message\":\"Success\",\"details\":\"Ok\"}") })),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application.json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-404",
                        value = "{\"statusCode\":\"404\",\"message\":\"Not Found\",\"details\":\"Food Menu Is Not Available\"}") })),
        @ApiResponse(responseCode = "405", description = "Method Not Allowed", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-405",
                        value = "{\"statusCode\":\"405\",\"message\":\"Method Not Allowed\",\"details\":\"This Food Menu Is Not Allowded\"}") })),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-400",
                        value = "{\"statusCode\":\"400\",\"message\":\"Bad Request\",\"details\":\"Please Enter Correct Value\"}") })),
        @ApiResponse(responseCode = "400", description = "Unsupported Media Type", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-415",
                        value = "{\"statusCode\":\"415\",\"message\":\"Unsupported Media Type\",\"details\":\"Please Enter Correct Value\"}") })) })
    public void deleteFoodMenuById(@PathVariable(name = "food-menu-id") long foodMenuId,
            @PathVariable(name = "admin-id") long userId) throws DynamoException {
        log.info("Leaving deleteFoodMenuById");
        foodMenuService.deleteFoodMenuById(foodMenuId, userId);
    }

    // View All Active Food Menu By User
    /**
     * Gets the all Active Food Menu with Food Items by User.
     * @param  userId          this id is used to find the user.
     * @return                 All the Active Food Menu with Food Items.
     * @throws DynamoException if foodMenu or User is not found.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user/{user-id}/food-menus")
    @Operation(method = "GET", summary = "Get the Active All Food Menus", description = "Get the Active All Food Menus")
    @Parameters(value = { @Parameter(allowEmptyValue = false, required = true, name = "userId",
            description = "Represents Users.", in = ParameterIn.PATH) })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success Payload",
                content = @Content(mediaType = "application.json",
                        schema = @Schema(implementation = FoodMenuViewUserDto.class),
                        examples = { @ExampleObject(name = "Success-200",
                                value = "{\"statusCode\":\"200\",\"message\":\"Success\",\"details\":\"Ok\"}") })),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application.json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-404",
                        value = "{\"statusCode\":\"404\",\"message\":\"Not Found\",\"details\":\"Food Menu Is Not Available\"}") })),
        @ApiResponse(responseCode = "405", description = "Method Not Allowed", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-405",
                        value = "{\"statusCode\":\"405\",\"message\":\"Method Not Allowed\",\"details\":\"This User Is Not Allowded\"}") })),
        @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(mediaType = "application.json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-409",
                        value = "{\"statusCode\":\"409\",\"message\":\"Conflict\",\"details\":\"Food Menu is Already Have.\"}") })),
        @ApiResponse(responseCode = "415", description = "Unsupported Media Type", content = @Content(
                mediaType = "application.json", schema = @Schema(implementation = ErrorResponse.class),
                examples = { @ExampleObject(name = "Error-415",
                        value = "{\"statusCode\":\"415\",\"message\":\"Unsupported Media Type\",\"details\":\"Please Enter Correct Value\"}") })) })
    public List<FoodMenuViewUserDto> viewAllActiveFoodMenuByUser(@PathVariable(name = "user-id") long userId)
            throws DynamoException {
        log.info("Entering viewAllActiveFoodMenu");
        List<FoodMenuViewUserDto> findFoodMenu = foodMenuService.viewAllActiveFoodMenuByUser(userId);
        log.info("Leaving viewAllActiveFoodMenu");
        return findFoodMenu;
    }
}
