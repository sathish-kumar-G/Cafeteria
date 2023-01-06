package net.breezeware.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import net.breezeware.entity.User;
import net.breezeware.exception.CustomException;
import net.breezeware.exception.ErrorResponse;
import net.breezeware.service.UserService;

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
 * UserController is used for create, view, update and delete the user. Autowired this UserController to UserService
 * Interface.
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    // Create New User
    @PostMapping("/user")
    @Operation(method = "POST", summary = "Create the User", description = "Create the User")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success Payload",
            content = @Content(mediaType = "application.json", schema = @Schema(implementation = User.class),
                    examples = { @ExampleObject(name = "Success-200",
                            value = "{\"statusCode\":\"200\",\"message\":\"Success\",\"details\":\"Ok\"}") })),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application.json",
                            schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Error-404",
                                    value = "{\"statusCode\":\"404\",\"message\":\"Not Found\",\"details\":\"User Is Not Available\"}") })),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed",
                    content = @Content(mediaType = "application.json",
                            schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Error-405",
                                    value = "{\"statusCode\":\"405\",\"message\":\"Method Not Allowed\",\"details\":\"This User Is Not Allowded\"}") })),
            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = @Content(mediaType = "application.json",
                            schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Error-409",
                                    value = "{\"statusCode\":\"409\",\"message\":\"Conflict\",\"details\":\"User is Already Have Account\"}") })),
            @ApiResponse(responseCode = "415", description = "Unsupported Media Type",
                    content = @Content(mediaType = "application.json",
                            schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Error-415",
                                    value = "{\"statusCode\":\"415\",\"message\":\"Unsupported Media Type\",\"details\":\"Please Enter Correct Value\"}") })) })

    public User createUser(@Valid @RequestBody User user) throws CustomException {
        log.info("Entering createUser {}", user);
        User saveUser = userService.createUser(user);
        log.info("Leaving createUser");
        return saveUser;
    }

    // Get All User
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users")
    @Operation(method = "GET", summary = "Get the Users", description = "Get the Users")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success Payload",
            content = @Content(mediaType = "application.json", schema = @Schema(implementation = User.class),
                    examples = { @ExampleObject(name = "Success-200",
                            value = "{\"statusCode\":\"200\",\"message\":\"Success\",\"details\":\"Ok\"}") })),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application.json",
                            schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Error-404",
                                    value = "{\"statusCode\":\"404\",\"message\":\"Not Found\",\"details\":\"User Is Not Available\"}") })),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed",
                    content = @Content(mediaType = "application.json",
                            schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Error-405",
                                    value = "{\"statusCode\":\"405\",\"message\":\"Method Not Allowed\",\"details\":\"This User Is Not Allowded\"}") })),
            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = @Content(mediaType = "application.json",
                            schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Error-409",
                                    value = "{\"statusCode\":\"409\",\"message\":\"Conflict\",\"details\":\"User is Already Have Account\"}") })),
            @ApiResponse(responseCode = "415", description = "Unsupported Media Type",
                    content = @Content(mediaType = "application.json",
                            schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Error-415",
                                    value = "{\"statusCode\":\"415\",\"message\":\"Unsupported Media Type\",\"details\":\"Please Enter Correct Value\"}") })) })
    public List<User> getUser() {
        log.info("Entering getUser");
        List<User> getUsers = userService.getUser();
        log.info("Leaving getUser");
        return getUsers;
    }

    // Get User BY Id
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_STAFF')")
    @GetMapping("/user/{user-id}")
    @Operation(method = "GET", summary = "Get the User.", description = "Get the User.")
    @Parameters(value = {
            @Parameter(allowEmptyValue = false, required = true, name = "userId", description = "Represents Users.",
                    in = ParameterIn.PATH) })
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success Payload",
            content = @Content(mediaType = "application.json", schema = @Schema(implementation = User.class),
                    examples = { @ExampleObject(name = "Success-200",
                            value = "{\"statusCode\":\"200\",\"message\":\"Success\",\"details\":\"Ok\"}") })),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application.json",
                            schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Error-404",
                                    value = "{\"statusCode\":\"404\",\"message\":\"Not Found\",\"details\":\"User Is Not Available\"}") })),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed",
                    content = @Content(mediaType = "application.json",
                            schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Error-405",
                                    value = "{\"statusCode\":\"405\",\"message\":\"Method Not Allowed\",\"details\":\"This User Is Not Allowded\"}") })),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application.json",
                            schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Error-400",
                                    value = "{\"statusCode\":\"400\",\"message\":\"Bad Request\",\"details\":\"Please Enter Correct Value\"}") })),
            @ApiResponse(responseCode = "415", description = "Unsupported Media Type",
                    content = @Content(mediaType = "application.json",
                            schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Error-415",
                                    value = "{\"statusCode\":\"415\",\"message\":\"Unsupported Media Type\",\"details\":\"Please Enter Correct Value\"}") })) })
    public User userFindById(@PathVariable(name = "user-id") long userId) throws CustomException {
        log.info("Entering userFindById");
        User getUserById = userService.userFindById(userId);
        log.info("Leaving userFindById");
        return getUserById;
    }

    // Update User By Id
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_STAFF')")
    @PutMapping("/user/{user-id}")
    @Operation(method = "PUT", summary = "Update the User", description = "Update the User")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    @Parameters(value = {
            @Parameter(allowEmptyValue = false, required = true, name = "userId", description = "Represents Users.",
                    in = ParameterIn.PATH) })
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success Payload",
            content = @Content(mediaType = "application.json", schema = @Schema(implementation = User.class),
                    examples = { @ExampleObject(name = "Success-200",
                            value = "{\"statusCode\":\"200\",\"message\":\"Success\",\"details\":\"Ok\"}") })),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application.json",
                            schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Error-404",
                                    value = "{\"statusCode\":\"404\",\"message\":\"Not Found\",\"details\":\"User Is Not Available\"}") })),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed",
                    content = @Content(mediaType = "application.json",
                            schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Error-405",
                                    value = "{\"statusCode\":\"405\",\"message\":\"Method Not Allowed\",\"details\":\"This User Is Not Allowded\"}") })),
            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = @Content(mediaType = "application.json",
                            schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Error-409",
                                    value = "{\"statusCode\":\"409\",\"message\":\"Conflict\",\"details\":\"User is Already Have Account\"}") })),
            @ApiResponse(responseCode = "415", description = "Unsupported Media Type",
                    content = @Content(mediaType = "application.json",
                            schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Error-415",
                                    value = "{\"statusCode\":\"415\",\"message\":\"Unsupported Media Type\",\"details\":\"Please Enter Correct Value\"}") })) })
    public User updateUserById(@PathVariable(name = "user-id") long userId, @Valid @RequestBody User user)
            throws CustomException {
        log.info("Entering updateUserById");
        User updateUser = userService.updateUserById(userId, user);
        log.info("Leaving updateUserById");
        return updateUser;
    }

    // Delete User By Id
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_STAFF')")
    @DeleteMapping("/user/{user-id}")
    @Operation(method = "DELETE", summary = "Delete the User.", description = "Delete the User.")
    @Parameters(value = {
            @Parameter(allowEmptyValue = false, required = true, name = "userId", description = "Represents Users.",
                    in = ParameterIn.PATH) })
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success Payload",
            content = @Content(mediaType = "application.json", schema = @Schema(implementation = User.class),
                    examples = { @ExampleObject(name = "Success-200",
                            value = "{\"statusCode\":\"200\",\"message\":\"Success\",\"details\":\"Ok\"}") })),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application.json",
                            schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Error-404",
                                    value = "{\"statusCode\":\"404\",\"message\":\"Not Found\",\"details\":\"User Is Not Available\"}") })),
            @ApiResponse(responseCode = "405", description = "Method Not Allowed",
                    content = @Content(mediaType = "application.json",
                            schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Error-405",
                                    value = "{\"statusCode\":\"405\",\"message\":\"Method Not Allowed\",\"details\":\"This User Is Not Allowded\"}") })),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application.json",
                            schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Error-400",
                                    value = "{\"statusCode\":\"400\",\"message\":\"Bad Request\",\"details\":\"Please Enter Correct Value\"}") })),
            @ApiResponse(responseCode = "400", description = "Unsupported Media Type",
                    content = @Content(mediaType = "application.json",
                            schema = @Schema(implementation = ErrorResponse.class), examples = {
                            @ExampleObject(name = "Error-415",
                                    value = "{\"statusCode\":\"415\",\"message\":\"Unsupported Media Type\",\"details\":\"Please Enter Correct Value\"}") })) })
    public void deleteUserById(@PathVariable(name = "user-id") long userId) throws CustomException {
        log.info("Entering deleteUserById");
        userService.deleteUserById(userId);
        log.info("Leaving deleteUserById");
    }
}
