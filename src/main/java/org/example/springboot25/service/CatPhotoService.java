package org.example.springboot25.service;

import jakarta.transaction.Transactional;
import org.example.springboot25.entities.CatPhoto;
import org.example.springboot25.repository.CatPhotoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class CatPhotoService {

    private final CatPhotoRepository catPhotoRepository;

    public CatPhotoService(CatPhotoRepository catPhotoRepository) {
        this.catPhotoRepository = catPhotoRepository;
    }

    // 🔹 Hämta alla bilder
    public List<CatPhoto> getAllCatPhotos() {
        return catPhotoRepository.findAll();
    }

    // 🔹 Hämta en bild baserat på ID
    public Optional<CatPhoto> getCatPhotoById(Long id) {
        return catPhotoRepository.findById(id);
    }

    // 🔹 Spara en ny bild
    public CatPhoto saveCatPhoto(CatPhoto catPhoto) {
        return catPhotoRepository.save(catPhoto);
    }

    // 🔹 Uppdatera en bild baserat på ID
    public Optional<CatPhoto> updateCatPhoto(Long id, CatPhoto updatedCatPhoto) {
        return catPhotoRepository.findById(id).map(existingCatPhoto -> {
            existingCatPhoto.setCatPhotoUrl(updatedCatPhoto.getCatPhotoUrl());
            existingCatPhoto.setCatPhotoCaption(updatedCatPhoto.getCatPhotoCaption());
            //existingCatPhoto.setCatPhotoCreatedAt(updatedCatPhoto.getCatPhotoCreatedAt());
            existingCatPhoto.setCatPhotoCat(updatedCatPhoto.getCatPhotoCat());
            return catPhotoRepository.save(existingCatPhoto);
        });
    }

    // 🔹 Ta bort en bild baserat på ID
    public boolean deleteCatPhoto(Long id) {
        if (catPhotoRepository.existsById(id)) {
            catPhotoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}