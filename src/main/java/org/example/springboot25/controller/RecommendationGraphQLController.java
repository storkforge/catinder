package org.example.springboot25.controller;

import org.example.springboot25.dto.RecommendationInputDTO;
import org.example.springboot25.dto.RecommendationOutputDTO;
import org.example.springboot25.dto.RecommendationUpdateDTO;
import org.example.springboot25.entities.Cat;
import org.example.springboot25.entities.Recommendation;
import org.example.springboot25.entities.User;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.mapper.RecommendationMapper;
import org.example.springboot25.service.CatService;
import org.example.springboot25.service.RecommendationService;
import org.example.springboot25.service.UserService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class RecommendationGraphQLController {

    private final RecommendationService recommendationService;
    private final RecommendationMapper recommendationMapper;
    private final UserService userService;
    private final CatService catService;

    public RecommendationGraphQLController(RecommendationService recommendationService, RecommendationMapper recommendationMapper, UserService userService, CatService catService) {
        this.recommendationService = recommendationService;
        this.recommendationMapper = recommendationMapper;
        this.userService = userService;
        this.catService = catService;
    }

    @QueryMapping
    public List<RecommendationOutputDTO> getAllRecommendations() {
        return recommendationService.getAllRecommendations().stream()
                .map(recommendationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @QueryMapping
    public RecommendationOutputDTO getRecommendationById(@Argument Long id) {
        Recommendation recommendation = recommendationService.getRecommendationById(id);
        return recommendationMapper.toDTO(recommendation);
    }

    @MutationMapping
    public RecommendationOutputDTO createRecommendation(@Argument("input") RecommendationInputDTO input) {
        User user = userService.findUserById(input.getUserId());
        Cat cat = catService.findCatById(input.getCatId());

        Recommendation recommendation = recommendationService.createRecommendation
                (recommendationMapper.toEntityInput(input, user, cat));
        return recommendationMapper.toDTO(recommendation);
    }

    @MutationMapping
    public RecommendationOutputDTO updateRecommendation(@Argument Long id, @Argument("input") RecommendationUpdateDTO input) {
        User user = userService.findUserById(input.getUserId());
        Cat cat = catService.findCatById(input.getCatId());

        Recommendation updatedRecommendation = recommendationService.updateRecommendation(id, recommendationMapper.toEntityUpdate(input, recommendationService.getRecommendationById(id), user, cat));
        return recommendationMapper.toDTO(updatedRecommendation);
    }

    @MutationMapping
    public boolean deleteRecommendation(@Argument Long id) {
        try {
            recommendationService.deleteRecommendation(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}

