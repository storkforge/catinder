package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.dto.CatInputDTO;
import org.example.springboot25.dto.CatOutputDTO;
import org.example.springboot25.dto.CatUpdateDTO;
import org.example.springboot25.entities.User;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.service.CatService;
import org.example.springboot25.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/cats")
public class CatViewController {

    private static final Logger log = LoggerFactory.getLogger(CatViewController.class);

    private final CatService catService;
    private final UserService userService;

    public CatViewController(CatService catService, UserService userService) {
        this.catService = catService;
        this.userService = userService;
    }

    @GetMapping
    public String showAllCats(Model model) {
        List<CatOutputDTO> cats = catService.getAllCats();
        model.addAttribute("cats", cats);
        return "cat/cats-list";
    }

    @GetMapping("/{catId}")
    public String showCatDetail(@PathVariable Long catId, Model model) {
        CatOutputDTO cat = catService.getCatDtoById(catId);
        model.addAttribute("cat", cat);
        return "cat/cat-detail";
    }

    @GetMapping("/new")
    public String showCreateNewCatForm(Model model) {
        model.addAttribute("cat", new CatInputDTO());
        return "cat/creating-a-new-cat-form";
    }

    @PostMapping
    public String processCreateNewCatForm(@ModelAttribute("cat") @Valid CatInputDTO catDTO,
                                          Principal principal,
                                          RedirectAttributes redirectAttributes) {
        if (principal instanceof OAuth2AuthenticationToken oauthToken) {
            OAuth2User oauth2User = oauthToken.getPrincipal();
            String email = oauth2User.getAttribute("email");
            User user = userService.findUserByEmail(email);

            catDTO.setUserId(user.getUserId());
            catService.addCat(catDTO);

            redirectAttributes.addFlashAttribute("create_success", "Cat created!");
        } else {
            throw new IllegalStateException("Unsupported principal type: " + principal.getClass());
        }
        return "redirect:/cats";
    }


    @GetMapping("/{catId}/edit")
    public String showEditCatForm(@PathVariable Long catId, Model model) {
        CatOutputDTO existingCat = catService.getCatDtoById(catId);
        CatUpdateDTO updateDTO = new CatUpdateDTO();
        updateDTO.setCatName(existingCat.getCatName());
        updateDTO.setCatProfilePicture(existingCat.getCatProfilePicture());
        updateDTO.setCatBreed(existingCat.getCatBreed());
        updateDTO.setCatGender(existingCat.getCatGender());
        updateDTO.setCatAge(existingCat.getCatAge());
        updateDTO.setCatPersonality(existingCat.getCatPersonality());

        model.addAttribute("cat", updateDTO);
        model.addAttribute("catId", catId);
        return "cat/existing-edit-cat-form";
    }

    //TODO: Add redirect to id?
    @PostMapping("/{catId}")
    public String updateCat(@PathVariable Long catId,
                            @ModelAttribute("cat") @Valid CatUpdateDTO updateDTO,
                            RedirectAttributes redirectAttributes) {
        try {
            catService.updateCat(catId, updateDTO);
            redirectAttributes.addFlashAttribute("update_success", "Cat updated!");
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cats";
    }

    @PostMapping("/{catId}/delete")
    public String deleteCat(@PathVariable Long catId, RedirectAttributes redirectAttributes) {
        try {
            catService.deleteCat(catId);
            redirectAttributes.addFlashAttribute("delete_success", "Cat deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete cat with id " + catId);
        }
        return "redirect:/cats";
    }
}
