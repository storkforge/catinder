package org.example.springboot25.mapper;

import org.example.springboot25.DTO.CatInputDTO;
import org.example.springboot25.DTO.CatOutputDTO;
import org.example.springboot25.DTO.CatUpdateDTO;
import org.example.springboot25.entities.Cat;
import org.example.springboot25.entities.User;
import org.example.springboot25.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class CatMapper {

    private final UserRepository userRepository;

    public CatMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Cat toCat (CatInputDTO catInputDTO) {
        Cat cat = new Cat();
        cat.setCatName(catInputDTO.getCatName());
        cat.setCatProfilePicture(catInputDTO.getCatProfilePicture());
        cat.setCatBreed(catInputDTO.getCatBreed());
        cat.setCatGender(catInputDTO.getCatGender());
        cat.setCatAge(catInputDTO.getCatAge());
        cat.setCatPersonality(catInputDTO.getCatPersonality());

        // TODO: Replace with UserNotFoundException once merged
        User user = userRepository.findById(catInputDTO.getUserId()).orElseThrow();
        cat.setUserCatOwner(user);
        return cat;
    }

    public void updateCatFromDTO(CatUpdateDTO catUpdateDTO, Cat cat) {
        if(catUpdateDTO.getCatName() != null) {
            cat.setCatName(catUpdateDTO.getCatName().trim());
        }
        if(catUpdateDTO.getCatProfilePicture() != null) {
            cat.setCatProfilePicture(catUpdateDTO.getCatProfilePicture().trim());
        }
        if(catUpdateDTO.getCatBreed() != null) {
            cat.setCatBreed(catUpdateDTO.getCatBreed().trim());
        }
        if(catUpdateDTO.getCatGender() != null) {
            cat.setCatGender(catUpdateDTO.getCatGender().trim());
        }
        if(catUpdateDTO.getCatAge() != null) {
            cat.setCatAge(catUpdateDTO.getCatAge());
        }
        if(catUpdateDTO.getCatPersonality() != null) {
            cat.setCatPersonality(catUpdateDTO.getCatPersonality().trim());
        }
    }

    public CatOutputDTO toDTO(Cat cat) {
        CatOutputDTO catOutputDTO = new CatOutputDTO();
        catOutputDTO.setCatId(cat.getCatId());
        catOutputDTO.setCatName(cat.getCatName());
        catOutputDTO.setCatProfilePicture(cat.getCatProfilePicture());
        catOutputDTO.setCatBreed(cat.getCatBreed());
        catOutputDTO.setCatGender(cat.getCatGender());
        catOutputDTO.setCatAge(cat.getCatAge());
        catOutputDTO.setCatPersonality(cat.getCatPersonality());
        return catOutputDTO;
    }
}
