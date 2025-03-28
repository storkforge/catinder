package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.entities.CatPhoto;
import org.example.springboot25.service.CatPhotoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/catphotos")
public class CatPhotoController {

    private final CatPhotoService catPhotoService;

    // 🔹 Constructor-based dependency injection (Recommended by String)
    public CatPhotoController(CatPhotoService catPhotoService) {
        this.catPhotoService = catPhotoService;
    }

    // 🔹 Get all Photos
    @GetMapping
    public List<CatPhoto> getAllCatPhotos() {
        return catPhotoService.getAllCatPhotos();
    }

    // 🔹 Get a photo based on ID
    @GetMapping("/{id}")
    public ResponseEntity<CatPhoto> getCatPhotoById(@PathVariable Long id) {
        Optional<CatPhoto> catPhoto = catPhotoService.getCatPhotoById(id);
        return catPhoto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Create a new CatPhoto
    @PostMapping
    public ResponseEntity<CatPhoto> createCatPhoto(@Valid @RequestBody CatPhoto catPhoto){
        CatPhoto saved  = catPhotoService.saveCatPhoto(catPhoto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // 🔹 Update an existing CatPhoto
    @PutMapping("/{id}")
    public ResponseEntity<CatPhoto> updateCatPhoto(@PathVariable Long id, @Valid @RequestBody CatPhoto updatedCatPhoto) {
        Optional<CatPhoto> updated = catPhotoService.updateCatPhoto(id, updatedCatPhoto);
        return updated.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Remove a CatPhoto based on ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCatPhoto(@PathVariable Long id) {
        boolean deleted = catPhotoService.deleteCatPhoto(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
