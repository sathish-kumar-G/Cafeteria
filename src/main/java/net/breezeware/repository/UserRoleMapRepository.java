package net.breezeware.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import net.breezeware.entity.FoodMenu;
import net.breezeware.entity.User;
import net.breezeware.entity.UserRoleMap;

public interface UserRoleMapRepository extends JpaRepository<UserRoleMap, Long> {

    Optional<UserRoleMap> findByUserId(User user);

    Optional<UserRoleMap> findByUserIdFirstName(String firstName);

    Optional<FoodMenu> findByUserIdUserId(long userId);

    void deleteByUserId(User existUser);

}
