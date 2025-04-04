package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.entities.CatPhoto;
import org.example.springboot25.service.CatPhotoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catphotos")
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
        CatPhoto catPhoto = catPhotoService.getCatPhotoById(id);
        return ResponseEntity.ok(catPhoto);
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
        CatPhoto updated = catPhotoService.updateCatPhoto(id, updatedCatPhoto);
        return ResponseEntity.ok(updated);
    }

    // 🔹 Remove a CatPhoto based on ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCatPhoto(@PathVariable Long id) {
        boolean deleted = catPhotoService.deleteCatPhoto(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
