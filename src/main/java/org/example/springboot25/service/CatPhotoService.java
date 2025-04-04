package org.example.springboot25.service;

import org.example.springboot25.entities.CatPhoto;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.repository.CatPhotoRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.List;

@Configuration
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
    public CatPhoto getCatPhotoById(Long id) {
        return catPhotoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("CatPhoto with id " + id + " not found"));
    }

    // 🔹 Save a new Photo
    public CatPhoto saveCatPhoto(CatPhoto catPhoto) {
        return catPhotoRepository.save(catPhoto);
    }

    // 🔹 Update a Photo based on ID
    public CatPhoto updateCatPhoto(Long id, CatPhoto updatedCatPhoto) {
        // Try to find the existing CatPhoto by id, throw NotFoundException if not found
        CatPhoto existingCatPhoto = catPhotoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("CatPhoto with id " + id + " not found"));

        // Update the existing photo fields
        existingCatPhoto.setCatPhotoUrl(updatedCatPhoto.getCatPhotoUrl());
        existingCatPhoto.setCatPhotoCaption(updatedCatPhoto.getCatPhotoCaption());
        // Note: We don't update catPhotoCat relationship here
        // Note: catPhotoCreatedAt is managed by @CreationTimestamp

        // Save the updated CatPhoto and return it
        return catPhotoRepository.save(existingCatPhoto);
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