package org.example.springboot25.service;

import org.example.springboot25.entities.User;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.repository.CatRepository;
import org.example.springboot25.entities.Cat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@ApplicationScope
public class CatService {

    private final CatRepository catRepository;

    /*
    @Autowired Injectar en instans av CatRepo.
     */
    @Autowired
    public CatService(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    public List<Cat> getAllCats() {
        return catRepository.findAll();
    }
    public List<Cat> getAllCatsByUser(User user) {
        return catRepository.findAllByUserCatOwner(user);
    }
    public Optional<Cat> getCatById(Long catId) {
        return catRepository.findById(catId);
    }

    //TODO: ADD BACK NOT NULL FOR USER IN SCHEMA, USER, CAT, CHECK CATRESTCONTROLLER AND CATVIEWCONTROLLER

    public Cat createCat(Cat cat) {
        return catRepository.save(cat);
    }

    //Fixa throws
    public Cat updateCat(Long catId, Cat catDetails) throws NotFoundException {
        return catRepository.findById(catId).map(cat -> {
            cat.setCatName(catDetails.getCatName());
            cat.setCatProfilePicture(catDetails.getCatProfilePicture());
            cat.setCatBreed(catDetails.getCatBreed());
            cat.setCatGender(catDetails.getCatGender());
            cat.setCatAge(catDetails.getCatAge());
            cat.setCatPersonality(catDetails.getCatPersonality());
            return catRepository.save(cat);
        }).orElseThrow(()-> new NotFoundException("Cat not found with id " + catId));
    }
//TODO KOLLA IGENOM PARTIAL UPDATE
    public Cat partialUpdateCat(Long catId, Map<String, Object> updates) throws NotFoundException {
        Cat cat = catRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Cat not found with id " + catId));
        if (updates.containsKey("catName")) {
            cat.setCatName((String) updates.get("catName"));
        }
        if (updates.containsKey("catProfilePicture")) {
            cat.setCatProfilePicture((String) updates.get("catProfilePicture"));
        }
        if (updates.containsKey("catBreed")) {
            cat.setCatBreed((String) updates.get("catBreed"));
        }
        if (updates.containsKey("catGender")) {
            cat.setCatGender((String) updates.get("catGender"));
        }
        if (updates.containsKey("catAge")) {
            Object catAgeObj = updates.get("catAge");
            if (catAgeObj instanceof Number) {
                cat.setCatAge(((Number) catAgeObj).intValue());
            }else if (catAgeObj instanceof String) {

                try{
                    int age =Integer.parseInt((String) catAgeObj);
                    cat.setCatAge(age);
                    } catch (NumberFormatException e) {
                    throw new NotFoundException("Invalid catAge value. Must be an integer.");
                }
            }else {
                throw new NotFoundException("Invalid type for catAge. Must be a number or numeric string.");
            }
            cat.setCatAge((int) updates.get("catAge"));
        }
        if (updates.containsKey("catPersonality")) {
            cat.setCatPersonality((String) updates.get("catPersonality"));
        }
        return catRepository.save(cat);
    }

    public void deleteCat(Long catId) throws NotFoundException {
        if(!catRepository.existsById(catId)) {
            throw new NotFoundException("Cat not found with id " + catId);
        }
        catRepository.deleteById(catId);
    }

}
