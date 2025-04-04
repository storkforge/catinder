package org.example.springboot25.repository;

import org.example.springboot25.entities.CatPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatPhotoRepository extends JpaRepository<CatPhoto, Long> {
}

