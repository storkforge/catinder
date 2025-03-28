package org.example.springboot25.service;

import org.example.springboot25.repository.CatRepository;
import org.example.springboot25.entities.Cat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
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
    public Optional<Cat> getCatById(Long catId) {
        return catRepository.findById(catId);
    }
    public Cat createCat(Cat cat) {
        return catRepository.save(cat);
    }

    public Cat updateCat(Long catId, Cat catDetails) throws Exception {
        return catRepository.findById(catId).map(cat -> {
            cat.setCatName(catDetails.getCatName());
            cat.setCatProfilePicture(catDetails.getCatProfilePicture());
            cat.setCatBreed(catDetails.getCatBreed());
            cat.setCatGender(catDetails.getCatGender());
            cat.setCatAge(catDetails.getCatAge());
            cat.setCatPersonality(catDetails.getCatPersonality());
            cat.setUserCatOwner(catDetails.getUserCatOwner());
            return catRepository.save(cat);
        }).orElseThrow(()-> new Exception("Cat not found with id " + catId));
    }

    public void deleteCat(Long catId) {
        catRepository.deleteById(catId);
    }


}
