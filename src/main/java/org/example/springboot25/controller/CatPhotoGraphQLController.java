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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CatPhotoGraphQLController {

    private static final Logger log = LoggerFactory.getLogger(CatPhotoGraphQLController.class);

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
        Cat cat = catService.findCatById(input.getCatPhotoCatId());

        CatPhoto catPhoto = catPhotoService.saveCatPhoto(
                catPhotoMapper.toEntityInput(input, cat)
        );

        return catPhotoMapper.toDTO(catPhoto);
    }

    @MutationMapping
    public CatPhotoOutputDTO updateCatPhoto(@Argument Long id, @Argument("input") @Valid CatPhotoUpdateDTO input) {
        CatPhoto existing = catPhotoService.getCatPhotoById(id)
                .orElseThrow(() -> new NotFoundException("CatPhoto with ID " + id + " not found"));

        Cat cat = existing.getCatPhotoCat();
        if (input.getCatPhotoCatId() != null) {
            cat = catService.findCatById(input.getCatPhotoCatId());
        }

        CatPhoto updated = catPhotoMapper.toEntityUpdate(existing, input, cat);

        return catPhotoService.updateCatPhoto(id, updated)
                .map(catPhotoMapper::toDTO)
                .orElseThrow(() -> new NotFoundException("Failed to update CatPhoto with ID " + id));
    }

    @MutationMapping
    public boolean deleteCatPhoto(@Argument Long id) {
        catPhotoService.getCatPhotoById(id)
                .orElseThrow(() -> new NotFoundException("CatPhoto with ID: " + id + " not found"));

        return catPhotoService.deleteCatPhoto(id);
    }
}