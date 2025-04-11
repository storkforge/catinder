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
import java.util.stream.Collectors;

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
                .collect(Collectors.toList());
    }

    @QueryMapping
    public CatPhotoOutputDTO getCatPhotoById(@Argument Long id) {
        CatPhoto catPhoto = catPhotoService.getCatPhotoById(id)
                .orElseThrow(() -> new NotFoundException("CatPhoto not found"));
        return catPhotoMapper.toDTO(catPhoto);
    }

    // Mutation: Create a new CatPhoto
    @MutationMapping
    public CatPhotoOutputDTO createCatPhoto(@Argument("input") @Valid CatPhotoInputDTO input) {
        Cat cat = catService.getCatById(input.getCatPhotoCatId())
                .orElseThrow(() -> new NotFoundException("Cat not found with ID: " + input.getCatPhotoCatId()));

        CatPhoto catPhoto = catPhotoService.saveCatPhoto(catPhotoMapper.toEntityInput(input, cat));
        return catPhotoMapper.toDTO(catPhoto);
    }

    @MutationMapping
    public CatPhotoOutputDTO updateCatPhoto(@Argument Long id, @Argument("input") CatPhotoUpdateDTO input) {
        Cat cat = catService.getCatById(input.getCatPhotoCatId())
                .orElseThrow(() -> new NotFoundException("Cat not found with ID: " + input.getCatPhotoCatId()));

        CatPhoto updatedCatPhoto = catPhotoService.updateCatPhoto(id, catPhotoMapper.toEntityUpdate(input, cat))
                .orElseThrow(() -> new NotFoundException("CatPhoto not found with ID: " + id));

        return catPhotoMapper.toDTO(updatedCatPhoto);
    }

    @MutationMapping
    public boolean deleteCatPhoto(@Argument Long id) {
        return catPhotoService.deleteCatPhoto(id);
    }
}

