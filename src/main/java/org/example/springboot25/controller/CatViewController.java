package org.example.springboot25.controller;

import jakarta.validation.Valid;
import org.example.springboot25.dto.CatInputDTO;
import org.example.springboot25.dto.CatOutputDTO;
import org.example.springboot25.entities.Cat;
import org.example.springboot25.entities.User;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.mapper.CatMapper;
import org.example.springboot25.service.CatService;
import org.example.springboot25.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/cats")
public class CatViewController {

    private final CatService catService;
    private final UserService userService;
    private final CatMapper catMapper;

    @Autowired
    public CatViewController(CatService catService, UserService userService, CatMapper catMapper) {
        this.catService = catService;
        this.userService = userService;
        this.catMapper = catMapper;
    }

    @GetMapping
    public String listCats(Model model,
                           @AuthenticationPrincipal OAuth2User principal,
                           Authentication authentication) {
        if (principal == null) {
            return "redirect:/login";
        }

        User me = userService.findUserByEmail(principal.getAttribute("email"));
        List<CatOutputDTO> cats = catService.getAllCatsByUserId(me.getUserId());
        model.addAttribute("cats", cats);
        return "cat/cats-list";
    }

    @GetMapping("/{catId}")
    public String showCatDetail(@PathVariable Long catId, Model model) {
        Cat cat = catService.getCatById(catId); // now returns Cat directly
        CatOutputDTO catDTO = catMapper.toDTO(cat);
        model.addAttribute("cat", catDTO);
        return "cat/cat-detail";
    }

    @GetMapping("/new")
    public String showCreateNewCatForm(Model model) {
        model.addAttribute("cat", new CatInputDTO());
        return "cat/creating-a-new-cat-form";
    }

    @PostMapping
    public String processCreateNewCatForm(
            @Valid @ModelAttribute("cat") CatInputDTO catDTO,
            BindingResult errors,
            Principal principal) {

        if (errors.hasErrors()) {
            return "cat/creating-a-new-cat-form";
        }

        if (principal instanceof OAuth2AuthenticationToken oauthToken) {
            OAuth2User oauth2User = oauthToken.getPrincipal();
            String email = oauth2User.getAttribute("email");

            User user = userService.findUserByEmail(email);
            catDTO.setUserId(user.getUserId());

            catService.createCat(catDTO);
        } else {
            return "redirect:/login";
        }

        return "redirect:/cats";
    }

    @GetMapping("/{catId}/edit")
    public String showEditExistingCatForm(@PathVariable Long catId, Model model) {
        Cat cat = catService.getCatById(catId); // no need to map Optional
        CatOutputDTO catDTO = catMapper.toDTO(cat);
        model.addAttribute("cat", catDTO);
        return "cat/existing-edit-cat-form";
    }

    @PostMapping("/{catId}")
    public String updateCat(@PathVariable Long catId, @ModelAttribute("cat") CatOutputDTO catDTO) {
        try {
            Long currentOwnerId = catService.getOwnerIdOfCat(catId);

            CatInputDTO inputDto = new CatInputDTO();
            inputDto.setCatName(catDTO.getCatName());
            inputDto.setCatProfilePicture(catDTO.getCatProfilePicture());
            inputDto.setCatBreed(catDTO.getCatBreed());
            inputDto.setCatGender(catDTO.getCatGender());
            inputDto.setCatAge(catDTO.getCatAge());
            inputDto.setCatPersonality(catDTO.getCatPersonality());
            inputDto.setUserId(currentOwnerId); // protect ownership

            catService.updateCat(catId, inputDto);
        } catch (Exception e) {
            throw new RuntimeException("Update failed for cat with id " + catId, e);
        }
        return "redirect:/cats";
    }

    @DeleteMapping("/{catId}/delete")
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
