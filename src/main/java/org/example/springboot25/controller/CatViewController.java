package org.example.springboot25.controller;

import org.example.springboot25.entities.Cat;
import org.example.springboot25.entities.User;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.service.CatService;
import org.example.springboot25.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/cats")
public class CatViewController {

    private final CatService catService;
    private final UserService userService;

    @Autowired
    public CatViewController(CatService catService, UserService userService) {

        this.catService = catService;
        this.userService = userService;
        }

    @GetMapping
    public String showAllCats(Model model) {
            List<Cat> cats = catService.getAllCats();
            model.addAttribute("cats", cats);
            return "cat/cats-list";
        }

    @GetMapping("/{catId}")
    public String showCatDetail(@PathVariable Long catId, Model model) {
        Cat cat = catService.getCatById(catId)
                .orElseThrow(()-> new NotFoundException("Cat not found with id " + catId));
        model.addAttribute("cat", cat);
        return "cat/cat-detail";
    }

    @GetMapping("/new")
    public String showCreateNewCatForm(Model model) {
        model.addAttribute("cat", new Cat());
        return "cat/creating-a-new-cat-form";
    }

    //Get UserService and add it later. Injected Principal for currently logged in user via security context.
    @PostMapping
    public String processCreateNewCatForm(@ModelAttribute("cat") Cat cat, Principal principal) {
        User user = userService.getUserByUserName(principal.getName());
        catService.createCat(cat);
        return "redirect:/cats";
    }

    @GetMapping("/{catId}/edit")
    public String showEditExistingCatForm(@PathVariable Long catId, Model model) {
        Cat cat = catService.getCatById(catId)
                .orElseThrow(()-> new NotFoundException("Cat not found with id " + catId));
        model.addAttribute("cat", cat);
        return "cat/existing-edit-cat-form";
    }

    @PostMapping("/{catId}")
    public String updateCat(@PathVariable Long catId, @ModelAttribute("cat") Cat cat) {
        try {
            catService.updateCat(catId, cat);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Update failed for cat with id " + catId, e);
        }
        return "redirect:/cats";
    }

    //Uppdatera till viewexception senare
    @GetMapping("/{catId}/delete")
    public String deleteCat(@PathVariable Long catId) {
        try {
            catService.deleteCat(catId);
            return "redirect:/cats";
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete cat with id " + catId, e);
        }
    }

}
