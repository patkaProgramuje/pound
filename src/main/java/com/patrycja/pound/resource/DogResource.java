package com.patrycja.pound.resource;

import com.patrycja.pound.models.dto.DogDTO;
import com.patrycja.pound.services.DogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dogs")
@RequiredArgsConstructor
public class DogResource {

    private final DogService dogService;

    @PostMapping
    public String add(@RequestBody DogDTO dogDTO) {
        return dogService.addDog(dogDTO);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        return dogService.deleteDog(id);
    }

    @GetMapping
    public List<DogDTO> get() {
        return dogService.getDogs();
    }

    @PutMapping("/{id}")
    public String update(@PathVariable("id") int id, @RequestBody DogDTO dogDTO) {
        return dogService.updateDog(id, dogDTO);
    }
}
