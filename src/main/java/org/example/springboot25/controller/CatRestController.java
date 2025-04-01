package org.example.springboot25.controller;

import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.service.CatService;
import org.example.springboot25.entities.Cat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cats")
public class CatRestController {

    private final CatService catService;

    /*
    @Autowired Injectar en instans av CatRepo.
     */
    @Autowired
    public CatRestController(CatService catService) {
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
        return ResponseEntity
                .created(URI.create("/api/cats/" + createdCat.getCatId()))
                .body(createdCat);
    }


    @PutMapping("/{catId}")
    public ResponseEntity<Cat> updateCat(@PathVariable Long catId, @RequestBody Cat catDetails) {
        try {
            Cat updatedCat = catService.updateCat(catId, catDetails);
            return ResponseEntity.ok(updatedCat);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<Cat> partialUpdateCat(@PathVariable Long catId, @RequestBody Map<String, Object> updates) {
        try {
            Cat updatedCat = catService.partialUpdateCat(catId, updates);
            return ResponseEntity.ok(updatedCat);
        }catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> deleteCat(@PathVariable Long catId) {
        try {
            catService.deleteCat(catId);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
