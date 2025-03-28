package org.example.springboot25.controller;

import org.example.springboot25.CatService;
import org.example.springboot25.entities.Cat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ResponseEntity<Cat> getCatById(@PathVariable Long id) {
        return catService.getCatById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @PostMapping
    public ResponseEntity<Cat> createCat(@RequestBody Cat cat) {
        Cat createdCat = catService.createCat(cat);
        return ResponseEntity.ok(createdCat);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cat> updateCat(@PathVariable Long id, @RequestBody Cat catDetails) {
        try {
            Cat updatedCat = catService.updateCat(id, catDetails);
            return ResponseEntity.ok(updatedCat);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCat(@PathVariable Long id) {
        catService.deleteCat(id);
        return ResponseEntity.noContent().build();
    }
}
