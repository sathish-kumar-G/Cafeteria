package net.breezeware.service;

import java.util.List;
import java.util.Optional;

import net.breezeware.dynamo.utils.exception.DynamoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import net.breezeware.entity.Role;
import net.breezeware.entity.User;
import net.breezeware.entity.UserRoleMap;
import net.breezeware.exception.CustomException;
import net.breezeware.repository.RoleRepository;
import net.breezeware.repository.UserRepository;
import net.breezeware.repository.UserRoleMapRepository;

/**
 * UserServiceImpl class is used to write the Business logic.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleMapRepository userRoleMapRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public User createUser(User user) throws DynamoException {
        // TODO Auto-generated method stub

        // Already User Available or not Condition Checking
        Optional<User> existUser = userRepository.findByEmailId(user.getEmailId());

        if (existUser.isPresent()) {
            throw new DynamoException("User Have Already Account ", HttpStatus.CONFLICT);
        }

        // Empty value Condition Checking
        if (user.equals(null)) {
            throw new DynamoException("Please fill the User Details", HttpStatus.BAD_REQUEST);
        }

        if (user.getFirstName().isEmpty()) {
            throw new DynamoException("Please fill the First Name", HttpStatus.BAD_REQUEST);
        }

        if (user.getLastName().isEmpty()) {
            throw new DynamoException("Please fill the Last Name", HttpStatus.BAD_REQUEST);
        }

        if (user.getEmailId().isEmpty() || user.getEmailId() == null) {
            throw new DynamoException("Please fill the  Emai ID", HttpStatus.BAD_REQUEST);
        }

        if (user.getPassword().isEmpty()) {
            throw new DynamoException("Please fill the  Password", HttpStatus.BAD_REQUEST);
        }

        // Get the Role of this User
        Role findRole = roleRepository.findById(user.getRoleId())
                .orElseThrow(() -> new DynamoException("please Fill the Correct Role Id", HttpStatus.BAD_REQUEST));

        // Save the User And Role in the Map table
        saveUserRoleMap(user, findRole);

        // Save the User
        return userRepository.save(user);
    }

    // Map user and role
    private void saveUserRoleMap(User user, Role findRole) {
        UserRoleMap map = new UserRoleMap();
        map.setUserId(user);
        map.setRoleId(findRole);
        userRoleMapRepository.save(map);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getUser() {
        // Get All Users
        List<User> users = userRepository.findAll();
        // For each loop is used for Get the Role Of the User
        for (User user : users) {
            Optional<UserRoleMap> map = userRoleMapRepository.findByUserId(user);
            user.setRoleId(map.get().getRoleId().getRoleId());
        }

        // Return All Users
        return users;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User userFindById(long userId) throws DynamoException {
        // User Available or not Condition Checking
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DynamoException("User is Not Found ", HttpStatus.NOT_FOUND));
        // User Role Get
        Optional<UserRoleMap> map = userRoleMapRepository.findByUserId(user);
        user.setRoleId(map.get().getRoleId().getRoleId());
        // return the user
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User updateUserById(long userId, User user) throws DynamoException {
        // User Available or not Condition Checking
        User updateUser = userRepository.findById(userId)
                .orElseThrow(() -> new DynamoException("User is Not Available", HttpStatus.NOT_FOUND));

        // Update the Users Record
        updateUser.setFirstName(user.getFirstName());
        updateUser.setLastName(user.getLastName());
        updateUser.setEmailId(user.getEmailId());
        updateUser.setPassword(user.getPassword());

        // Empty Value Checking
        if (updateUser.getFirstName().isEmpty()) {
            throw new DynamoException("Please fill the First Name", HttpStatus.BAD_REQUEST);
        }

        if (updateUser.getLastName().isEmpty()) {
            throw new DynamoException("Please fill the Last Name", HttpStatus.BAD_REQUEST);
        }

        if (updateUser.getEmailId().isEmpty()) {
            throw new DynamoException("Please fill the  Emai ID", HttpStatus.BAD_REQUEST);
        }

        if (updateUser.getPassword().isEmpty()) {
            throw new DynamoException("Please fill the  Password", HttpStatus.BAD_REQUEST);
        }

        // Save Updated User
        return userRepository.save(updateUser);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteUserById(long userId) throws DynamoException {
        // User Available or not Condition Checking
        User existUser = userRepository.findById(userId)
                .orElseThrow(() -> new DynamoException("User Is Not Available", HttpStatus.NOT_FOUND));

        // Delete this User in User Role Map table and User Table
        userRoleMapRepository.deleteByUserId(existUser);
        userRepository.deleteById(existUser.getUserId());

    }

}
