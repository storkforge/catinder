package org.example.springboot25.controller;

import org.example.springboot25.entities.CatPhoto;
import org.example.springboot25.services.CatPhotoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/catphotos")  // Grund-URL för denna controller
public class CatPhotoController {

    private final CatPhotoService catPhotoService;

    // 🔹 Konstruktor-baserad dependency injection (rekommenderat av Spring)
    public CatPhotoController(CatPhotoService catPhotoService) {
        this.catPhotoService = catPhotoService;
    }

    // 🔹 Hämta alla bilder
    @GetMapping
    public List<CatPhoto> getAllCatPhotos() {
        return catPhotoService.getAllCatPhotos();
    }

    // 🔹 Hämta en bild baserat på ID
    @GetMapping("/{id}")
    public ResponseEntity<CatPhoto> getCatPhotoById(@PathVariable Long id) {
        Optional<CatPhoto> catPhoto = catPhotoService.getCatPhotoById(id);
        return catPhoto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Skapa en ny CatPhoto
    @PostMapping
    public CatPhoto createCatPhoto(@RequestBody CatPhoto catPhoto) {
        return catPhotoService.saveCatPhoto(catPhoto);
    }

    // 🔹 Uppdatera en befintlig CatPhoto
    @PutMapping("/{id}")
    public ResponseEntity<CatPhoto> updateCatPhoto(@PathVariable Long id, @RequestBody CatPhoto updatedCatPhoto) {
        Optional<CatPhoto> updated = catPhotoService.updateCatPhoto(id, updatedCatPhoto);
        return updated.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Ta bort en CatPhoto baserat på ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCatPhoto(@PathVariable Long id) {
        boolean deleted = catPhotoService.deleteCatPhoto(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
