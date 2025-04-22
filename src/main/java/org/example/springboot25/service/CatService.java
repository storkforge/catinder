package org.example.springboot25.service;

import org.example.springboot25.dto.CatInputDTO;
import org.example.springboot25.dto.CatOutputDTO;
import org.example.springboot25.dto.CatUpdateDTO;
import org.example.springboot25.entities.Cat;
import org.example.springboot25.entities.CatGender;
import org.example.springboot25.entities.User;
import org.example.springboot25.exceptions.BadRequestException;
import org.example.springboot25.exceptions.InvalidInputException;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.mapper.CatMapper;
import org.example.springboot25.repository.CatRepository;
import org.example.springboot25.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.annotation.ApplicationScope;

import java.beans.PropertyEditorSupport;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@ApplicationScope
public class CatService {

    private static final Logger log = LoggerFactory.getLogger(CatService.class);
    private final CatRepository catRepository;
    private final UserRepository userRepository;
    private final CatMapper catMapper;

    @Autowired
    public CatService(CatRepository catRepository, UserRepository userRepository, CatMapper catMapper) {
        this.catRepository = catRepository;
        this.userRepository = userRepository;
        this.catMapper = catMapper;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(CatGender.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (text == null) {
                    throw new IllegalArgumentException("CatGender cannot be null");
                }
                try {
                    setValue(CatGender.valueOf(text.trim().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid catGender value. Must be 'MALE' or 'FEMALE'.");
                }
            }
        });
    }

    // ========================
    // Interna metoder (Entity)
    // ========================

    public Cat findCatById(Long catId) {
        return catRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Cat not found with id " + catId));
    }

    public Optional<Cat> getCatById(Long catId) {
        return catRepository.findById(catId);
    }

    public Cat createCat(Cat cat) {
        return catRepository.save(cat);
    }

    public Cat saveCat(Cat cat) {
        return catRepository.save(cat);
    }

    public Cat updateCat(Long catId, Cat catDetails) {
        return catRepository.findById(catId).map(cat -> {
            cat.setCatName(catDetails.getCatName());
            cat.setCatProfilePicture(catDetails.getCatProfilePicture());
            cat.setCatBreed(catDetails.getCatBreed());
            cat.setCatGender(catDetails.getCatGender());
            cat.setCatAge(catDetails.getCatAge());
            cat.setCatPersonality(catDetails.getCatPersonality());
            return catRepository.save(cat);
        }).orElseThrow(() -> new NotFoundException("Cat not found with id " + catId));
    }

    public void deleteCatById(Long catId) {
        if (!catRepository.existsById(catId)) {
            throw new NotFoundException("Cat not found with id " + catId);
        }
        log.info("Deleting cat with id: {}", catId);
        catRepository.deleteById(catId);
    }

    public void deleteCat(Long catId) {
        deleteCatById(catId);
    }

    // ========================
    // Externa metoder (DTO)
    // ========================

    public List<CatOutputDTO> getAllCats() {
        return catRepository.findAll().stream().map(catMapper::toDto).toList();
    }

    public List<CatOutputDTO> getAllCatsByUser(User user) {
        return catRepository.findAllByUserCatOwner(user).stream().map(catMapper::toDto).toList();
    }

    public List<CatOutputDTO> getCatsByName(String name) {
        return catRepository.findByCatNameContainingIgnoreCase(name).stream().map(catMapper::toDto).toList();
    }

    public CatOutputDTO getCatDtoById(Long id) {
        return catMapper.toDto(findCatById(id));
    }

    public CatOutputDTO addCat(CatInputDTO dto) {
        log.info("Creating new cat: {}", dto.getCatName());
        Cat cat = catMapper.toCat(dto);
        return catMapper.toDto(saveCat(cat));
    }

    public Cat addCat(Cat cat) {
        log.info("Saving cat: {}", cat.getCatName());
        return saveCat(cat);
    }

    public CatOutputDTO updateCat(Long catId, CatUpdateDTO dto) {
        Cat cat = findCatById(catId);
        log.info("Updating cat: {}", cat.getCatName());
        catMapper.updateCatFromDto(dto, cat);
        return catMapper.toDto(saveCat(cat));
    }

    public CatOutputDTO partialUpdateCat(Long catId, Map<String, Object> updates) {
        Cat cat = findCatById(catId);

        if (updates.containsKey("catName")) cat.setCatName((String) updates.get("catName"));
        if (updates.containsKey("catProfilePicture")) cat.setCatProfilePicture((String) updates.get("catProfilePicture"));
        if (updates.containsKey("catBreed")) cat.setCatBreed((String) updates.get("catBreed"));

        if (updates.containsKey("catGender")) {
            Object genderObj = updates.get("catGender");
            if (genderObj instanceof String genderStr) {
                try {
                    cat.setCatGender(CatGender.valueOf(genderStr.trim().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    throw new BadRequestException("Invalid catGender value. Must be 'MALE' or 'FEMALE'.");
                }
            }
        }

        if (updates.containsKey("catAge")) {
            Object catAgeObj = updates.get("catAge");
            try {
                if (catAgeObj instanceof Number number) {
                    cat.setCatAge(number.intValue());
                } else if (catAgeObj instanceof String str) {
                    cat.setCatAge(Integer.parseInt(str));
                }
            } catch (NumberFormatException e) {
                throw new InvalidInputException("Invalid catAge value: " + catAgeObj);
            }
        }

        if (updates.containsKey("catPersonality")) cat.setCatPersonality((String) updates.get("catPersonality"));

        return catMapper.toDto(saveCat(cat));
    }
}
