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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/cats")
public class CatViewController {

    private static final Logger log = LoggerFactory.getLogger(CatViewController.class);
    private final CatService catService;
    private final UserService userService;

    private static final List<String> CAT_BREEDS = Arrays.asList(
            "Siamese", "Persian", "Maine Coon", "Ragdoll", "Bengal",
            "British Shorthair", "Scottish Fold", "Sphynx", "Abyssinian", "Birman"
    );

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
    public String showCatDetail(@PathVariable Long catId, Model model, Principal principal) {
        CatOutputDTO cat = catService.getCatDtoById(catId);
        User currentUser = userService.findUserByEmail(getEmailFromPrincipal(principal));

        if (!cat.getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("Du kan bara se dina egna katter.");
        }

        model.addAttribute("cat", cat);
        return "cat/cat-detail";
    }

    @GetMapping("/new")
    public String showCreateNewCatForm(Model model) {
        CatInputDTO catInput = new CatInputDTO();
        model.addAttribute("cat", catInput);
        model.addAttribute("breeds", CAT_BREEDS);
        return "cat/creating-a-new-cat-form";
    }

    @PostMapping
    public String processCreateNewCatForm(@ModelAttribute("cat") @Valid CatInputDTO catDTO,
                                          Principal principal,
                                          RedirectAttributes redirectAttributes) {
        User user = userService.findUserByEmail(getEmailFromPrincipal(principal));
        catDTO.setUserId(user.getUserId());
        catService.addCat(catDTO);
        redirectAttributes.addFlashAttribute("create_success", "Cat created!");
        return "redirect:/cats";
    }

    @GetMapping("/{catId}/edit")
    public String showEditCatForm(@PathVariable Long catId, Model model, Principal principal) {
        CatOutputDTO cat = catService.getCatDtoById(catId);
        User currentUser = userService.findUserByEmail(getEmailFromPrincipal(principal));

        if (!cat.getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("Du kan bara redigera dina egna katter.");
        }

        CatUpdateDTO updateDTO = new CatUpdateDTO();
        updateDTO.setCatName(cat.getCatName());
        updateDTO.setCatProfilePicture(cat.getCatProfilePicture());
        updateDTO.setCatBreed(cat.getCatBreed());
        updateDTO.setCatGender(cat.getCatGender());
        updateDTO.setCatAge(cat.getCatAge());
        updateDTO.setCatPersonality(cat.getCatPersonality());

        model.addAttribute("cat", updateDTO);
        model.addAttribute("breeds", CAT_BREEDS);
        model.addAttribute("catId", catId);
        return "cat/existing-edit-cat-form";
    }

    @PostMapping("/{catId}")
    public String updateCat(@PathVariable Long catId,
                            @ModelAttribute("cat") @Valid CatUpdateDTO updateDTO,
                            Principal principal,
                            RedirectAttributes redirectAttributes) {
        CatOutputDTO cat = catService.getCatDtoById(catId);
        User currentUser = userService.findUserByEmail(getEmailFromPrincipal(principal));

        if (!cat.getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("Du kan bara uppdatera dina egna katter.");
        }

        try {
            catService.updateCat(catId, updateDTO);
            redirectAttributes.addFlashAttribute("update_success", "Cat updated!");
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cats";
    }

    @PostMapping("/{catId}/delete")
    public String deleteCat(@PathVariable Long catId,
                            Principal principal,
                            RedirectAttributes redirectAttributes) {
        try {
            CatOutputDTO cat = catService.getCatDtoById(catId);
            User currentUser = userService.findUserByEmail(getEmailFromPrincipal(principal));

            if (!cat.getUserId().equals(currentUser.getUserId())) {
                throw new AccessDeniedException("Du kan bara ta bort dina egna katter.");
            }

            catService.deleteCat(catId);
            redirectAttributes.addFlashAttribute("delete_success", "Cat deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete cat with id " + catId);
        }
        return "redirect:/cats";
    }

    private String getEmailFromPrincipal(Principal principal) {
        if (principal instanceof OAuth2AuthenticationToken oauthToken) {
            OAuth2User oauth2User = oauthToken.getPrincipal();
            return oauth2User.getAttribute("email");
        }
        throw new IllegalStateException("Unsupported principal type: " + principal.getClass().getName());
    }
}
