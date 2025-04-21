package org.example.springboot25.mapper;

import org.example.springboot25.dto.CatInputDTO;
import org.example.springboot25.dto.CatOutputDTO;
import org.example.springboot25.dto.CatUpdateDTO;
import org.example.springboot25.entities.Cat;
import org.example.springboot25.entities.CatGender;
import org.example.springboot25.entities.User;
import org.example.springboot25.exceptions.NotFoundException;
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
        cat.setCatName(catInputDTO.getCatName().trim());
        cat.setCatProfilePicture(catInputDTO.getCatProfilePicture() != null ?
                catInputDTO.getCatProfilePicture().trim() : null);
        cat.setCatBreed(catInputDTO.getCatBreed().trim());
        if (catInputDTO.getCatGender() != null) {
            cat.setCatGender(CatGender.valueOf(catInputDTO.getCatGender().trim().toUpperCase()));
        } else {
            cat.setCatGender(null);  // or handle as needed
        }
        cat.setCatAge(catInputDTO.getCatAge());
        cat.setCatPersonality(catInputDTO.getCatPersonality() != null ?
                catInputDTO.getCatPersonality().trim() : null);

        User user = userRepository.findById(catInputDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("User with ID " + catInputDTO.getUserId() + " not found"));

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
        if (catUpdateDTO.getCatGender() != null) {
            cat.setCatGender(CatGender.valueOf(catUpdateDTO.getCatGender().trim().toUpperCase()));
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
        catOutputDTO.setCatGender(cat.getCatGender() != null ? cat.getCatGender().name() : null);
        catOutputDTO.setCatAge(cat.getCatAge());
        catOutputDTO.setCatPersonality(cat.getCatPersonality());
        if (cat.getUserCatOwner() != null) {
            catOutputDTO.setUserId(cat.getUserCatOwner().getUserId());
        }
        return catOutputDTO;
    }
}
