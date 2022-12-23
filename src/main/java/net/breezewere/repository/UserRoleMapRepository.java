package net.breezewere.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import net.breezewere.entity.FoodMenu;
import net.breezewere.entity.User;
import net.breezewere.entity.UserRoleMap;

public interface UserRoleMapRepository extends JpaRepository<UserRoleMap, Long> {

    Optional<UserRoleMap> findByUserId(User user);

    Optional<UserRoleMap> findByUserIdFirstName(String firstName);

    Optional<FoodMenu> findByUserIdUserId(long userId);

    void deleteByUserId(User existUser);

}
