package org.example.springboot25.repository;

import org.example.springboot25.entities.Recommendation;
import org.example.springboot25.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    List<Recommendation> findAllByUser(User user);
}