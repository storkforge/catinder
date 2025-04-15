package org.example.springboot25.service;

import org.example.springboot25.dto.CatOutputDTO;
import org.example.springboot25.entities.User;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.mapper.CatMapper;
import org.example.springboot25.repository.CatRepository;
import org.example.springboot25.entities.Cat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
//@Transactional
@ApplicationScope
public class CatService {

    private static final Logger log = LoggerFactory.getLogger(CatService.class);

    private final CatRepository catRepository;
    private final CatMapper catMapper;

    public CatService(CatRepository catRepository, CatMapper catMapper) {
        this.catRepository = catRepository;
        this.catMapper = catMapper;
    }

    public List<CatOutputDTO> getAllCatsByOwner(User user) {
        return catRepository.findAllByUserCatOwner(user).stream()
                .map(this::toOutputDto)
                .toList();
    }

    public CatOutputDTO getCatById(Long catId, User currentUser) {
        Cat cat = getCatEntityById(catId)
                .orElseThrow(() -> new NotFoundException("Cat not found"));

        if (cat.getUserCatOwner() == null || !cat.getUserCatOwner().getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("You can only view your own cats.");
        }
        return toOutputDto(cat);
    }

    public CatOutputDTO toOutputDto(Cat cat) {
        return catMapper.toDto(cat);
    }

    public CatOutputDTO createCat(CatInputDTO inputDTO, User owner) {
        Cat cat = catMapper.toEntity(inputDTO);
        cat.setUserCatOwner(owner);
        log.info("Creating cat for user: {}", owner.getUserName());
        return catMapper.toDto(catRepository.save(cat));
    }

    public CatOutputDTO updateCat(Long catId, CatUpdateDTO updateDTO) {
        Cat cat = catRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Cat not found"));

        catMapper.updateEntityFromDto(updateDTO, cat);
        log.info("Updating cat with ID: {}", catId);
        return catMapper.toDto(catRepository.save(cat));
    }

    public CatOutputDTO partialUpdateCat(Long catId, Map<String, Object> updates) {
        Cat cat = catRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Cat not found"));

        if (updates.containsKey("catName")) {
            cat.setCatName((String) updates.get("catName"));
        }
        if (updates.containsKey("catAge")) {
            cat.setCatAge((Integer) updates.get("catAge"));
        }
        if (updates.containsKey("catBreed")) {
            cat.setCatBreed((String) updates.get("catBreed"));
        }
        if (updates.containsKey("catGender")) {
            cat.setCatBreed((String) updates.get("catGender"));
        }

        log.info("Partially updated cat with ID: {}", catId);
        return catMapper.toDto(catRepository.save(cat));
    }

    public void deleteCat(Long id) {
        log.info("Deleting cat with ID: {}", id);
        catRepository.deleteById(id);
    }
}