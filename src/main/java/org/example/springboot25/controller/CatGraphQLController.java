package org.example.springboot25.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.example.springboot25.dto.CatInputDTO;
import org.example.springboot25.dto.CatOutputDTO;
import org.example.springboot25.dto.CatUpdateDTO;
import org.example.springboot25.entities.Cat;
import org.example.springboot25.exceptions.CatNotFoundException;
import org.example.springboot25.mapper.CatMapper;
import org.example.springboot25.service.CatService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CatGraphQLController {

    private final CatService catService;
    private final CatMapper catMapper;

    public CatGraphQLController(CatService catService, CatMapper catMapper) {
        this.catService = catService;
        this.catMapper = catMapper;
    }

    @QueryMapping
    public List<CatOutputDTO> getAllCats() {
        return catService.getAllCats().stream().map(catMapper::toDTO).toList();
    }

    // ToDo: CatService needs an update on getCatById-method to make this method easier
    @QueryMapping
    public CatOutputDTO getCatById(@Argument Long catId) {
        Cat cat = catService.getCatById(catId)
                .orElseThrow(() -> new EntityNotFoundException("Cat with ID " + catId + " not found"));
        return catMapper.toDTO(cat);
    }

      // ToDo: CatService needs an getCatByName-method for this to work:
//    @QueryMapping
//    public CatOutputDTO getCatByName(@Argument String catName) {
//        return catMapper.toDTO(catService.getCatByName(catName));
//    }


    @MutationMapping
    public CatOutputDTO createCat(@Argument("input") @Valid CatInputDTO input) {
        Cat cat = catMapper.toCat(input);
        Cat savedCat = catService.createCat(cat);
        return catMapper.toDTO(savedCat);
    }

    // ToDo: CatService need getCatByName-method, to make this method easier
    @MutationMapping
    public CatOutputDTO updateCat(@Argument Long catId, @Argument("input") @Valid CatUpdateDTO input) {
        Cat catToUpdate = catService.getCatById(catId)
                .orElseThrow(() -> new CatNotFoundException("Cat with ID " + catId + " not found"));

        catMapper.updateCatFromDTO(input, catToUpdate);
        Cat updatedCat = catService.updateCat(catId, catToUpdate);
        return catMapper.toDTO(updatedCat);
    }

    @MutationMapping
    public boolean deleteCat(@Argument Long catId) {
        catService.deleteCat(catId);
        return true;
    }
}
