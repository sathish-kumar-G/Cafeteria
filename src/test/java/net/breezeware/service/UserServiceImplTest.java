package net.breezeware.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import net.breezeware.entity.Role;
import net.breezeware.entity.User;
import net.breezeware.entity.UserRoleMap;
import net.breezeware.exception.CustomException;
import net.breezeware.repository.RoleRepository;
import net.breezeware.repository.UserRepository;
import net.breezeware.repository.UserRoleMapRepository;

@SpringBootTest
// @ExtendWith(MockitoExtension.class)
@DisplayName("User Test")
class UserServiceImplTest {

    @Mock
    UserRepository repository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    UserRoleMapRepository mapRepository;

    @InjectMocks
    UserServiceImpl service;

    // User Instance create before All Test case
    User user;

    @BeforeEach
    void init() {
        user = new User();
    }

    // Create User test Case
    @Test
    @DisplayName("Create New User Test")
    void testCreateUser() throws CustomException {
        // User user = new User();
        Role role = new Role();
        UserRoleMap map = new UserRoleMap();

        user.setFirstName("sathish");
        user.setLastName("kumar");
        user.setEmailId("sathish@gmail.com");
        user.setPassword("sathish@123");

        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(role));

        user.setRoleId(1);

        when(mapRepository.save(any(UserRoleMap.class))).thenReturn(map);

        when(repository.save(any(User.class))).thenReturn(user);
        User savedUser = service.createUser(user);

        // verify(repository).save(any(User.class));

        // assertThat(savedUser).isNotNull();
        assertEquals(user, savedUser);
    }

    /*
     * @Test void testCreateUser_withNullInput_throwsCustomException() throws
     * CustomException { Assertions.assertThrows(NullPointerException.class, () -> {
     * service.createUser(null); }); }
     */
    @Test
    @DisplayName("Exception Test case")
    void testCreateUser_withNullEmailId_throwsCustomException() throws CustomException {
        user.setFirstName("sathish");
        user.setLastName("kumar");
        user.setEmailId("");
        user.setPassword("sathish@123");

        assertThrows(CustomException.class, () -> {
            service.createUser(user);
        });
    }

    // Get All User TestCase
    @Test
    @DisplayName("Get All User Test")
    void testGetUser() {
        // User user = new User();
        // User case
        List<User> users = new ArrayList<>();
        users.add(user);
        when(repository.findAll()).thenReturn(users);

        // Role case
        Role role = new Role();

        // User Role Map case
        UserRoleMap map = new UserRoleMap();
        map.setRoleId(role);
        when(mapRepository.findByUserId(user)).thenReturn(Optional.of(map));

        List<User> foundUsers = service.getUser();

        assertEquals(users, foundUsers);

    }

    // Test Find by id case
    @Test
    @DisplayName("User Find By Id Test")
    void testUserFindById() throws CustomException {

        // User user = new User();

        // User case
        when(repository.findById(anyLong())).thenReturn(Optional.of(user));

        // Role case
        Role role = new Role();

        // User Role Map case
        UserRoleMap map = new UserRoleMap();
        map.setRoleId(role);
        when(mapRepository.findByUserId(user)).thenReturn(Optional.of(map));

        User foundUser = service.userFindById(1L);

        assertEquals(foundUser, user);
    }

    // Update User By Id Test Case
    @Test
    @DisplayName("Update User Test case")
    void testUpdateUserById() throws CustomException {

        when(repository.findById(user.getUserId())).thenReturn(Optional.of(user));

        user.setFirstName("sathish");
        user.setLastName("kumar");
        user.setEmailId("sk@gmailcom");
        user.setPassword("sathish");

        when(repository.save(any(User.class))).thenReturn(user);

        User actualUser = service.updateUserById(user.getUserId(), user);

        assertEquals(user, actualUser);
    }

    // Delete User BY Id Test Case
    @Test
    @DisplayName("Delete User Test Case")
    void testDeleteUserById() throws CustomException {

        repository.save(user);

        repository.deleteById(user.getUserId());

        Optional<User> deleteId = repository.findById(user.getUserId());
        assertThat(deleteId).isEmpty();
    }

}
