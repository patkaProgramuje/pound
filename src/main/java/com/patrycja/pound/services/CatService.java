package com.patrycja.pound.services;

import com.patrycja.pound.enums.CatColor;
import com.patrycja.pound.models.domain.Cat;
import com.patrycja.pound.models.domain.Zookeeper;
import com.patrycja.pound.models.dto.CatDTO;
import com.patrycja.pound.repository.CatRepository;
import com.patrycja.pound.services.mappers.CatMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CatService {

    private final CatRepository catRepository;
    private final CatMapper catMapper;
    private final ZookeeperService zookeeperService;

    public List<CatDTO> getAllCats(String sort) {
        List<Cat> cats = catRepository.findAll();
        if (sort.toLowerCase().equals("desc")) {
            Collections.reverse(cats);
        }
        return cats.stream()
                .map(catMapper::map)
                .collect(Collectors.toList());
    }

    public String getCatName(int id) {
        return catRepository.findById(id)
                .map(Cat::getName)
                .orElseThrow(() -> new IllegalArgumentException("This cat do not have name."));
    }

    public Cat getCatById(int id) {
        return catRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No cat was found with this id."));
    }

    public String addCat(CatDTO catDTO) {
        Cat cat = catMapper.map(catDTO);
        Zookeeper zookeeper = zookeeperService.findFreeZookeeper();
        cat.setZookeeper(zookeeper);
        catRepository.save(cat);
        zookeeperService.saveAnimalToZookeeper(cat, zookeeper);
        return ResponseEntity.ok("Successfully added kitty").toString();
    }

    public String deleteCat(int id) {
        Cat cat = catRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found cat for this id."));
        zookeeperService.deleteAnimalFromZookeeper(cat);
        catRepository.delete(cat);
        return ResponseEntity.ok("Successfully deleted kitty").toString();
    }

    public String updateCat(int id, CatDTO catDTO) {
        Cat updateCat = catRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No cat founded"));
        updateCat.setAge(catDTO.getAge());
        updateCat.setColor(catDTO.getColor());
        updateCat.setName(catDTO.getName());
        catRepository.save(updateCat);
        return ResponseEntity.ok("Cat was updated successfully.").toString();
    }

    public List<CatDTO> getCatByColor(String color) {
        return getAllCats("").stream()
                .filter(c -> c.getColor() == CatColor.valueOf(color.toUpperCase()))
                .collect(Collectors.toList());
    }
}
