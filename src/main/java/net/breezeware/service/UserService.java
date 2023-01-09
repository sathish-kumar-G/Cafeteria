package net.breezeware.service;

import java.util.List;

import net.breezeware.entity.User;
import net.breezeware.exception.CustomException;

/**
 * UserServiceImpl implements UserService Interface.
 */
public interface UserService {

    /**
     * Register or Create the new User.
     * @param  user            Object is used to set the new User details.
     * @return                 User details and Success Response.
     * @throws CustomException if the data is null or already a user registered.
     */
    // Create User
    User createUser(User user) throws CustomException;

    /**
     * Gets the all User.
     * @return All User details.
     */
    // View All User
    List<User> getUser();

    /**
     * Gets the user by id.
     * @param  userId          this id is used to find the user.
     * @return                 the user.
     * @throws CustomException if user is not found.
     */
    // Find User By Id
    User userFindById(long userId) throws CustomException;

    /**
     * Update the user by id.
     * @param  user            this user data is update.
     * @param  userId          this id is used to find the user.
     * @return                 updated user.
     * @throws CustomException if user is not found.
     */
    // Update User By Id
    User updateUserById(long userId, User user) throws CustomException;

    /**
     * Delete the user by id.
     * @param  userId          this id is used to find the user.
     * @throws CustomException if user is not found.
     */
    // Delete User By Id
    void deleteUserById(long userId) throws CustomException;

}
