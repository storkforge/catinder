package org.example.springboot25;

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
    public Optional<Cat> getCatById(Long id) {
        return catRepository.findById(id);
    }
    public Cat createCat(Cat cat) {
        return catRepository.save(cat);
    }

    public Cat updateCat(Long id, Cat catDetails) throws Exception {
        return catRepository.findById(id).map(cat -> {
            cat.setCatName(catDetails.getCatName());
            cat.setCatProfilePicture(catDetails.getCatProfilePicture());
            cat.setCatBreed(catDetails.getCatBreed());
            cat.setCatGender(catDetails.getCatGender());
            cat.setCatAge(catDetails.getCatAge());
            cat.setCatPersonality(catDetails.getCatPersonality());
            cat.setUserCatOwner(catDetails.getUserCatOwner());
            return catRepository.save(cat);
        }).orElseThrow(()-> new Exception("Cat not found with id " + id));
    }

    public void deleteCat(Long id) {
        catRepository.deleteById(id);
    }


}
