package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.dto.CatPhotoInputDTO;
import org.example.springboot25.dto.CatPhotoOutputDTO;
import org.example.springboot25.dto.CatPhotoUpdateDTO;
import org.example.springboot25.entities.Cat;
import org.example.springboot25.entities.CatPhoto;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.mapper.CatPhotoMapper;
import org.example.springboot25.service.CatPhotoService;
import org.example.springboot25.service.CatService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class CatPhotoGraphQLController {

    private final CatPhotoService catPhotoService;
    private final CatPhotoMapper catPhotoMapper;
    private final CatService catService;

    public CatPhotoGraphQLController(CatPhotoService catPhotoService, CatPhotoMapper catPhotoMapper, CatService catService) {
        this.catPhotoService = catPhotoService;
        this.catPhotoMapper = catPhotoMapper;
        this.catService = catService;
    }

    @QueryMapping
    public List<CatPhotoOutputDTO> getAllCatPhotos() {
        return catPhotoService.getAllCatPhotos().stream()
                .map(catPhotoMapper::toDTO)
                .toList();
    }

    @QueryMapping
    public CatPhotoOutputDTO getCatPhotoById(@Argument("catPhotoId") Long id) {
        CatPhoto catPhoto = catPhotoService.getCatPhotoById(id)
                .orElseThrow(() -> new NotFoundException("CatPhoto with ID: " + id + " not found"));
        return catPhotoMapper.toDTO(catPhoto);
    }


    @MutationMapping
    public CatPhotoOutputDTO createCatPhoto(@Argument("input") @Valid CatPhotoInputDTO input) {
        Cat cat = catService.getCatById(input.getCatPhotoCatId())
                .orElseThrow(() -> new NotFoundException("Cat with ID: " + input.getCatPhotoCatId() + " not found"));

        CatPhoto catPhoto = catPhotoService.saveCatPhoto(catPhotoMapper.toEntityInput(input, cat));
        return catPhotoMapper.toDTO(catPhoto);
    }

    @MutationMapping
    public CatPhotoOutputDTO updateCatPhoto(@Argument Long id, @Argument("input") CatPhotoUpdateDTO input) {
        Optional<CatPhoto> optionalExisting = catPhotoService.getCatPhotoById(id);
        if (optionalExisting.isEmpty()) {
            throw new NotFoundException("CatPhoto with ID " + id + " not found");
        }
        CatPhoto existing = optionalExisting.get();

        Cat cat = existing.getCatPhotoCat();
        if (input.getCatPhotoCatId() != null) {
            Optional<Cat> optionalCat = catService.getCatById(input.getCatPhotoCatId());
            if (optionalCat.isEmpty()) {
                throw new NotFoundException("Cat with ID " + input.getCatPhotoCatId() + " not found");
            }
            cat = optionalCat.get();
        }

        CatPhoto updated = catPhotoMapper.toEntityUpdate(existing, input, cat);

        Optional<CatPhoto> optionalUpdated = catPhotoService.updateCatPhoto(id, updated);
        if (optionalUpdated.isEmpty()) {
            throw new NotFoundException("Failed to update CatPhoto with ID " + id);
        }

        return catPhotoMapper.toDTO(optionalUpdated.get());
    }



    @MutationMapping
    public boolean deleteCatPhoto(@Argument Long id) {
        catPhotoService.getCatPhotoById(id)
                .orElseThrow(() -> new NotFoundException("CatPhoto with ID: " + id  + " not found"));
        return catPhotoService.deleteCatPhoto(id);
    }
}

