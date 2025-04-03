package org.example.springboot25.service;

import org.example.springboot25.entities.CatPhoto;
import org.example.springboot25.repositories.CatPhotoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CatPhotoService {

    private final CatPhotoRepository catPhotoRepository;

    public CatPhotoService(CatPhotoRepository catPhotoRepository) {
        this.catPhotoRepository = catPhotoRepository;
    }

    // 🔹 Get all Photos
    public List<CatPhoto> getAllCatPhotos() {
        return catPhotoRepository.findAll();
    }

    // 🔹 Get all Photos based on ID
    public Optional<CatPhoto> getCatPhotoById(Long id) {
        return catPhotoRepository.findById(id);
    }

    // 🔹 Save a new Photo
    public CatPhoto saveCatPhoto(CatPhoto catPhoto) {
        return catPhotoRepository.save(catPhoto);
    }

    // 🔹 Update a Photo based on ID
    public Optional<CatPhoto> updateCatPhoto(Long id, CatPhoto updatedCatPhoto) {
        return catPhotoRepository.findById(id).map(existingCatPhoto -> {
            existingCatPhoto.setCatPhotoUrl(updatedCatPhoto.getCatPhotoUrl());
            existingCatPhoto.setCatPhotoCaption(updatedCatPhoto.getCatPhotoCaption());
            return catPhotoRepository.save(existingCatPhoto);
        });
    }

    // 🔹 Remove a Photo based on ID
    public boolean deleteCatPhoto(Long id) {
        if (catPhotoRepository.existsById(id)) {
            catPhotoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}