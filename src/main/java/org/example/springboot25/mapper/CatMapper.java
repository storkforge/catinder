package org.example.springboot25.mapper;

import org.example.springboot25.dto.CatInputDTO;
import org.example.springboot25.dto.CatOutputDTO;
import org.example.springboot25.dto.CatUpdateDTO;
import org.example.springboot25.entities.Cat;
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

    public Cat toCat(CatInputDTO dto) {
        Cat cat = new Cat();
        cat.setCatName(dto.getCatName().trim());
        cat.setCatProfilePicture(dto.getCatProfilePicture() != null ? dto.getCatProfilePicture().trim() : null);
        cat.setCatBreed(dto.getCatBreed().trim());
        cat.setCatGender(dto.getCatGender().trim());
        cat.setCatAge(dto.getCatAge());
        cat.setCatPersonality(dto.getCatPersonality() != null ? dto.getCatPersonality().trim() : null);

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("User with ID " + dto.getUserId() + " not found"));

        cat.setUserCatOwner(user);
        return cat;
    }

    public void updateCatFromDTO(CatUpdateDTO dto, Cat cat) {
        if (dto.getCatName() != null) {
            cat.setCatName(dto.getCatName().trim());
        }
        if (dto.getCatProfilePicture() != null) {
            cat.setCatProfilePicture(dto.getCatProfilePicture().trim());
        }
        if (dto.getCatBreed() != null) {
            cat.setCatBreed(dto.getCatBreed().trim());
        }
        if (dto.getCatGender() != null) {
            cat.setCatGender(dto.getCatGender().trim());
        }
        if (dto.getCatAge() != null) {
            cat.setCatAge(dto.getCatAge());
        }
        if (dto.getCatPersonality() != null) {
            cat.setCatPersonality(dto.getCatPersonality().trim());
        }
    }

    public CatOutputDTO toDTO(Cat cat) {
        CatOutputDTO dto = new CatOutputDTO();
        dto.setCatId(cat.getCatId());
        dto.setCatName(cat.getCatName());
        dto.setCatProfilePicture(cat.getCatProfilePicture());
        dto.setCatBreed(cat.getCatBreed());
        dto.setCatGender(cat.getCatGender());
        dto.setCatAge(cat.getCatAge());
        dto.setCatPersonality(cat.getCatPersonality());
        if (cat.getUserCatOwner() != null) {
            dto.setUserId(cat.getUserCatOwner().getUserId());
        }
        return dto;
    }

    public void updateCatFromInputDTO(CatInputDTO dto, Cat cat) {
        cat.setCatName(dto.getCatName().trim());
        cat.setCatProfilePicture(dto.getCatProfilePicture() != null ? dto.getCatProfilePicture().trim() : null);
        cat.setCatBreed(dto.getCatBreed().trim());
        cat.setCatGender(dto.getCatGender().trim());
        cat.setCatAge(dto.getCatAge());
        cat.setCatPersonality(dto.getCatPersonality() != null ? dto.getCatPersonality().trim() : null);
    }
}
