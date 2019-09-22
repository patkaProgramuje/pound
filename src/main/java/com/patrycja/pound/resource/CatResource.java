package com.patrycja.pound.resource;

import com.patrycja.pound.models.dto.CatDTO;
import com.patrycja.pound.services.CatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cats")
public class CatResource {

    private CatService catService;

    public CatResource(CatService catService) {
        this.catService = catService;
    }

    @GetMapping
    public List<CatDTO> getListAllCats(@RequestParam(value = "sort", defaultValue = "") String sort) {
        return catService.getAllCats(sort);
    }

    @GetMapping("/colors/{color}")
    public List<CatDTO> getCatByColor(@PathVariable("color") String color) {
        return catService.getCatByColor(color);
    }

    @PostMapping
    public String addCat(@RequestBody CatDTO catDTO) {
        return catService.addCat(catDTO);
    }

    @DeleteMapping("/{id}")
    public String deleteCat(@PathVariable("id") int id) {
        return catService.deleteCat(id);
    }

    @PutMapping("/{id}")
    public String updateCat(@PathVariable("id") int id, @RequestBody CatDTO catDTO) {
        return catService.updateCat(id, catDTO);
    }
}
