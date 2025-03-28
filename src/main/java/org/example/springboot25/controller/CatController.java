package org.example.springboot25.controller;

import org.example.springboot25.CatService;
import org.example.springboot25.entities.Cat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cats")
public class CatController {

    private final CatService catService;

    /*
    @Autowired Injectar en instans av CatRepo.
     */
    @Autowired
    public CatController(CatService catService) {
        this.catService = catService;
    }

    @GetMapping
    public List<Cat> getAllCats() {
        return catService.getAllCats();
    }

    @GetMapping("/{catId}")
    public ResponseEntity<Cat> getCatById(@PathVariable Long catId) {
        return catService.getCatById(catId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @PostMapping
    public ResponseEntity<Cat> createCat(@RequestBody Cat cat) {
        Cat createdCat = catService.createCat(cat);
        return ResponseEntity.ok(createdCat);
    }

    @PutMapping("/{catId}")
    public ResponseEntity<Cat> updateCat(@PathVariable Long catId, @RequestBody Cat catDetails) {
        try {
            Cat updatedCat = catService.updateCat(catId, catDetails);
            return ResponseEntity.ok(updatedCat);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> deleteCat(@PathVariable Long catId) {
        catService.deleteCat(catId);
        return ResponseEntity.noContent().build();
    }
}
