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
        if (dto == null) throw new IllegalArgumentException("CatInputDTO cannot be null");

        Cat cat = new Cat();
        cat.setCatName(dto.getCatName());
        cat.setCatProfilePicture(dto.getCatProfilePicture());
        cat.setCatBreed(dto.getCatBreed());
        cat.setCatGender(dto.getCatGender());
        cat.setCatAge(dto.getCatAge());
        cat.setCatPersonality(dto.getCatPersonality());

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("User with ID " + dto.getUserId() + " not found"));

        cat.setUserCatOwner(user);
        return cat;
    }

    public void updateCatFromDto(CatUpdateDTO dto, Cat cat) {
        if (dto == null || cat == null) throw new IllegalArgumentException("DTO or Cat cannot be null");

        if (dto.getCatName() != null) cat.setCatName(dto.getCatName());
        if (dto.getCatProfilePicture() != null) cat.setCatProfilePicture(dto.getCatProfilePicture());
        if (dto.getCatBreed() != null) cat.setCatBreed(dto.getCatBreed());
        if (dto.getCatGender() != null) cat.setCatGender(dto.getCatGender());
        if (dto.getCatAge() != null) cat.setCatAge(dto.getCatAge());
        if (dto.getCatPersonality() != null) cat.setCatPersonality(dto.getCatPersonality());
    }

    public CatOutputDTO toDto(Cat cat) {
        if (cat == null) throw new IllegalArgumentException("Cat cannot be null");

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
}
