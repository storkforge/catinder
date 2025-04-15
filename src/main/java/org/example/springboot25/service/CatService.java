package org.example.springboot25.service;

import org.example.springboot25.dto.CatInputDTO;
import org.example.springboot25.dto.CatOutputDTO;
import org.example.springboot25.entities.User;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.mapper.CatMapper;
import org.example.springboot25.repository.CatRepository;
import org.example.springboot25.entities.Cat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    public CatOutputDTO getCatByIdForUser(Long catId, User currentUser) {
        Cat cat = findByIdOrThrow(catId);
        verifyOwnership(cat, currentUser);
        return catMapper.toDTO(cat);
    }

    public CatOutputDTO createCat(CatInputDTO inputDTO, User owner) {
        Cat cat = catMapper.toCat(inputDTO);
        cat.setUserCatOwner(owner);
        log.info("Creating cat for user: {}", owner.getUserName());
        return catMapper.toDTO(catRepository.save(cat));
    }

    public CatOutputDTO updateCat(Long catId, CatUpdateDTO updateDTO, User currentUser) {
        Cat cat = findByIdOrThrow(catId);
        verifyOwnership(cat, currentUser);

        catMapper.updateCatFromDTO(updateDTO, cat);
        log.info("Updating cat with ID: {}", catId);
        return catMapper.toDTO(catRepository.save(cat));
    }

    public CatOutputDTO partialUpdateCat(Long catId, Map<String, Object> updates, User currentUser) {
        Cat cat = findByIdOrThrow(catId);
        verifyOwnership(cat, currentUser);

        applyPartialUpdates(cat, updates);
        log.info("Partially updated cat with ID: {}", catId);
        return catMapper.toDTO(catRepository.save(cat));
    }

    public void deleteCat(Long id, User currentUser) {
        Cat cat = findByIdOrThrow(id);
        verifyOwnership(cat, currentUser);

        log.info("Deleting cat with ID: {}", id);
        catRepository.deleteById(id);
    }

    // ---- Interna metoder (endast entiteter används här) ----

    private Cat findByIdOrThrow(Long catId) {
        return catRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Cat with ID " + catId + " not found"));
    }

    private void verifyOwnership(Cat cat, User user) {
        if (cat.getUserCatOwner() == null || !cat.getUserCatOwner().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("You can only access your own cats.");
        }
    }

    private void applyPartialUpdates(Cat cat, Map<String, Object> updates) {
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
            cat.setCatGender((String) updates.get("catGender"));
        }
        // Lägg till fler fält här om du behöver
    }
}