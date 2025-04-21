package org.example.springboot25.service;

import org.example.springboot25.dto.CatInputDTO;
import org.example.springboot25.dto.CatOutputDTO;
import org.example.springboot25.dto.CatUpdateDTO;
import org.example.springboot25.entities.Cat;
import org.example.springboot25.entities.User;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.mapper.CatMapper;
import org.example.springboot25.repository.CatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@ApplicationScope
public class CatService {

    private final CatRepository catRepository;
    private final CatMapper catMapper;

    @Autowired
    public CatService(CatRepository catRepository, CatMapper catMapper) {
        this.catRepository = catRepository;
        this.catMapper = catMapper;
    }

    @Cacheable("cats")
    public List<CatOutputDTO> getAllCats() {
        return catRepository.findAll()
                .stream()
                .map(catMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Cacheable("cats")
    public List<CatOutputDTO> getAllCatsByUser(User user) {
        return catRepository.findAllByUserCatOwner(user)
                .stream()
                .map(catMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<Cat> getAllCatsByUserAsEntity(User user) {
        return catRepository.findAllByUserCatOwner(user);
    }

    public List<Cat> getCatsVisibleTo(Authentication auth, User currentUser) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return isAdmin ? catRepository.findAll() : catRepository.findAllByUserCatOwner(currentUser);
    }

    @Cacheable(value = "cat", key = "#catId")
    public Optional<Cat> getCatById(Long catId) {
        return catRepository.findById(catId);
    }

    @CacheEvict(value = {"cats", "cat"}, allEntries = true)
    public CatOutputDTO createCat(CatInputDTO dto) {
        Cat cat = catMapper.toCat(dto);
        return catMapper.toDTO(catRepository.save(cat));
    }

    @CachePut(value = "cat", key = "#catId")
    @CacheEvict(value = "cats", allEntries = true)
    public CatOutputDTO updateCat(Long catId, CatInputDTO dto) throws NotFoundException {
        return catRepository.findById(catId).map(cat -> {
            catMapper.updateCatFromInputDTO(dto, cat);
            return catMapper.toDTO(catRepository.save(cat));
        }).orElseThrow(() -> new NotFoundException("Cat not found with id " + catId));
    }

    @CachePut(value = "cat", key = "#catId")
    @CacheEvict(value = "cats", allEntries = true)
    public CatOutputDTO partialUpdateCat(Long catId, CatUpdateDTO updates) throws NotFoundException {
        Cat cat = catRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Cat not found with id " + catId));
        catMapper.updateCatFromDTO(updates, cat);
        return catMapper.toDTO(catRepository.save(cat));
    }

    @CacheEvict(value = {"cats", "cat"}, allEntries = true)
    public void deleteCat(Long catId) throws NotFoundException {
        if (!catRepository.existsById(catId)) {
            throw new NotFoundException("Cat not found with id " + catId);
        }
        catRepository.deleteById(catId);
    }
}
