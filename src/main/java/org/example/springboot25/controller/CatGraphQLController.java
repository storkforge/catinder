package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.dto.CatInputDTO;
import org.example.springboot25.dto.CatOutputDTO;
import org.example.springboot25.dto.CatUpdateDTO;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.service.CatService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Controller
@Validated
public class CatGraphQLController {

    private final CatService catService;

    public CatGraphQLController(CatService catService) {
        this.catService = catService;
    }

    @QueryMapping
    public List<CatOutputDTO> getAllCats() {
        return catService.getAllCats();
    }

    @QueryMapping
    public CatOutputDTO getCatById(@Argument Long catId) {
        return catService.getCatDtoById(catId);
    }

    @MutationMapping
    public CatOutputDTO createCat(@Argument("input") @Valid CatInputDTO input) {
        return catService.addCat(input);
    }

    @MutationMapping
    public CatOutputDTO updateCat(@Argument Long catId, @Argument("input") @Valid CatUpdateDTO input) {
        return catService.updateCat(catId, input);
    }

    @MutationMapping
    public boolean deleteCat(@Argument Long catId) {
        try {
            catService.deleteCat(catId);
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }
}
