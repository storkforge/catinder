package org.example.springboot25.controller;

import org.example.springboot25.entities.Cat;
import org.example.springboot25.exceptions.NotFoundException;
import org.example.springboot25.service.CatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/cats")
public class CatViewController {

    private final CatService catService;

    @Autowired
    public CatViewController(CatService catService) {
            this.catService = catService;
        }

    @GetMapping
    public String showAllCats(Model model) {
            List<Cat> cats = catService.getAllCats();
            model.addAttribute("cats", cats);
            return "cats-list";
        }

    @GetMapping("/{catId}")
    public String showCatDetail(@PathVariable Long catId, Model model) {
        Cat cat = catService.getCatById(catID)
                .orElseThrow(()-> new NotFoundException("Cat not found with id " + catId));
        model.addAttribute("cat", cat);
        return "cat-detail";
    }

    @GetMapping("/new")
    public String showCreateNewCatForm(Model model) {
        model.addAttribute("cat", new Cat());
        return "cat-form";
    }

    @PostMapping
    public String processCreateNewCatForm(@ModelAttribute("cat") Cat cat) {
        catService.createCat(cat);
        return "redirect:/cats";
    }

    @GetMapping("/{catId}/edit")
    public String showEditCatForm(@PathVariable Long catId, Model model) {
        Cat cat = catService.getCatById(catId)
                .orElseThrow(()-> new NotFoundException("Cat not found with id " + catId));
        model.addAttribute("cat", cat);
        return "cat-form";
    }

    @PostMapping("/{catID}")
    public String updateCat(@PathVariable Long catId, @ModelAttribute("cat") Cat cat) {
        try {
            catService.updateCat(catId,cat);
        }catch (Exception e) {
            throw new NotFoundException("Update failed for cat with id " + catId);
        }
        return "redirect:/cats";
    }

    @GetMapping("/{catID}/delete")
    public String deleteCat(@PathVariable Long catId) {
        catService.deleteCat(catId);
        return "redirect:/cats";
    }

}
