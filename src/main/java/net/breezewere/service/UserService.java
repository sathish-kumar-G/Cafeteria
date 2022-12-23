package net.breezewere.service;

import java.util.List;

import net.breezewere.entity.User;
import net.breezewere.exception.CustomException;

public interface UserService {

    /**
     * Create New User
     * @param  user            create New user.
     * @return                 store the new user in user repository.
     * @throws CustomException If User is already there.
     */
    // Create User
    User createUser(User user) throws CustomException;

    /**
     * View All User
     * @return users Get the all users in user repository.
     */
    // View All User
    List<User> getUser();

    /**
     * View User By Id
     * @param  userId          Get the user by this id.
     * @return                 Return the user.
     * @throws CustomException If the user is not exits.
     */
    // Find User By Id
    User userFindById(long userId) throws CustomException;

    /**
     * Update the User By Id
     * @param  userId          Update the user by this id.
     * @param  user            Update this user.
     * @return                 Update the user in user repository.
     * @throws CustomException If the user is not there.
     */
    // Update User By Id
    User updateUserById(long userId, User user) throws CustomException;

    /**
     * Delete the user By Id.
     * @param  userId          Delete the user by this id.
     * @return                 Delete Success message.
     * @throws CustomException If the user is not there.
     */
    // Delete User By Id
    void deleteUserById(long userId) throws CustomException;

}
