package org.example.springboot25.repository;

import org.example.springboot25.entities.User;
import org.example.springboot25.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserName(String userName);
    boolean existsByUserEmail(String userEmail);

    Optional<User> findByUserName(String userName);
    Optional<User> findByUserEmail(String userEmail);
    List<User> findAllByUserNameContainingIgnoreCase(String userName);
    List<User> findAllByUserFullNameContainingIgnoreCase(String fullName);
    List<User> findAllByUserLocationIgnoreCase(String location);
    List<User> findAllByUserRole(UserRole role);

    @Query("SELECT u FROM User u WHERE u.userName LIKE %:userName%")
    List<User> findAllByUserName(String userName);

    @Query("SELECT u FROM User u WHERE u.userFullName LIKE %:userFullName%")
    List<User> findAllByUserFullName(String userFullName);

    @Query("SELECT u FROM User u WHERE u.userLocation = :userLocation")
    List<User> findAllByLocation(String userLocation);

    @Query("SELECT u FROM User u WHERE u.userRole = :userRole")
    List<User> findAllByRole(String userRole);

    @Query("SELECT u FROM User u WHERE u.userRole = :userRole AND u.userLocation = :userLocation")
    List<User> findAllByRoleAndLocation(String userRole, String userLocation);

    @Query("SELECT u FROM User u JOIN u.userCats c WHERE c.catName LIKE %:catName%")
    List<User> findAllUsersByCatName(String catName);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN u.userCats c " +
            "WHERE u.userName LIKE %:searchTerm% OR c.catName LIKE %:searchTerm%")
    List<User> findByUserNameOrCatName(@Param("searchTerm") String searchTerm);
}
