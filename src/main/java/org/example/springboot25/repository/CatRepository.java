package org.example.springboot25.repository;

import org.example.springboot25.entities.Cat;
import org.example.springboot25.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CatRepository extends JpaRepository<Cat, Long> {
    List<Cat> findAllByUserCatOwner(User user);
}
