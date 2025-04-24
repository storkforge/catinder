package org.example.springboot25.service;

import org.example.springboot25.dto.CatInputDTO;
import org.example.springboot25.dto.CatOutputDTO;
import org.example.springboot25.dto.CatUpdateDTO;
import org.example.springboot25.entities.Cat;
import org.example.springboot25.entities.User;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.mapper.CatMapper;
import org.example.springboot25.repository.CatRepository;
import org.example.springboot25.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@ApplicationScope
public class CatService {

    private final CatRepository catRepository;
    private final CatMapper catMapper;
    private final UserRepository userRepository;

    @Autowired
    public CatService(CatRepository catRepository, CatMapper catMapper, UserRepository userRepository) {
        this.catRepository = catRepository;
        this.catMapper = catMapper;
        this.userRepository = userRepository;
    }

    @Cacheable(value = "cats", key = "'allCats'")
    public List<CatOutputDTO> getAllCats() {
        return catRepository.findAll()
                .stream()
                .map(catMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "cats", key = "#userId")
    public List<CatOutputDTO> getAllCatsByUserId(Long userId) {
        return catRepository.findAllByUserCatOwnerUserId(userId)
                .stream()
                .map(catMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Deprecated
    public List<CatOutputDTO> getAllCatsByUser(User user) {
        return getAllCatsByUserId(user.getUserId());
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
    public Cat getCatById(Long catId) {
        return catRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Cat not found with id " + catId));
    }

    public Cat findCatById(Long catId) {
        return getCatById(catId);
    }

    public Long getOwnerIdOfCat(Long catId) {
        return catRepository.findById(catId)
                .map(cat -> {
                    if (cat.getUserCatOwner() == null) {
                        throw new NotFoundException("Owner not found for cat with id " + catId);
                    }
                    return cat.getUserCatOwner().getUserId();
                })
                .orElseThrow(() -> new NotFoundException("Cat not found with id " + catId));
    }

    @CacheEvict(value = "cats", allEntries = true)
    public CatOutputDTO createCat(CatInputDTO dto) {
        if (!userRepository.existsById(dto.getUserId())) {
            throw new NotFoundException("User not found with id " + dto.getUserId());
        }

        Cat cat = catMapper.toCat(dto);
        return catMapper.toDTO(catRepository.save(cat));
    }

    @CachePut(value = "cat", key = "#catId")
    @CacheEvict(value = "cats", allEntries = true)
    public CatOutputDTO updateCat(Long catId, CatInputDTO dto) throws NotFoundException {
        return catRepository.findById(catId).map(cat -> {
            // keep the original owner
            Long originalOwnerId = cat.getUserCatOwner().getUserId();

            catMapper.updateCatFromInputDTO(dto, cat);

            // enforce ownership protection
            if (!originalOwnerId.equals(cat.getUserCatOwner().getUserId())) {
                cat.setUserCatOwner(userRepository.getReferenceById(originalOwnerId));
            }

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

    @Caching(evict = {
            @CacheEvict(value = "cat", key = "#catId"),
            @CacheEvict(value = "cats", allEntries = true)
    })
    public void deleteCat(Long catId) throws NotFoundException {
        if (!catRepository.existsById(catId)) {
            throw new NotFoundException("Cat not found with id " + catId);
        }
        catRepository.deleteById(catId);
    }
}
