package com.patrycja.pound.services;

import com.patrycja.pound.models.domain.Dog;
import com.patrycja.pound.models.domain.Zookeeper;
import com.patrycja.pound.models.dto.DogDTO;
import com.patrycja.pound.repository.DogRepository;
import com.patrycja.pound.services.mappers.DogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DogService {

    private final DogRepository dogRepository;
    private final DogMapper dogMapper;
    private final ZookeeperService zookeeperService;

    public String addDog(DogDTO dogDTO) {
        Dog dog = dogMapper.map(dogDTO);
        Zookeeper zookeeper = zookeeperService.findFreeZookeeper();
        dog.setZookeeper(zookeeper);
        dogRepository.save(dog);
        zookeeperService.saveAnimalToZookeeper(dog, zookeeper);
        return "Successfully added a dog.";
    }

    public String deleteDog(int id) {
        Dog dog = dogRepository.getOne(id);
        zookeeperService.deleteAnimalFromZookeeper(dog);
        dogRepository.deleteById(id);
        return ResponseEntity.ok("Successfully deleted dog.").toString();
    }

    public List<DogDTO> getDogs() {
        return dogRepository.findAll().stream()
                .map(dogMapper::map)
                .collect(Collectors.toList());
    }

    public String updateDog(int id, DogDTO dogDTO) {
        Dog oldDog = dogRepository.getOne(id);
        if (oldDog == null) {
            throw new IllegalArgumentException("No cat founded");
        }
        oldDog.setNumberOfTooth(dogDTO.getNumberOfTooth());
        oldDog.setAge(dogDTO.getAge());
        oldDog.setName(dogDTO.getName());
        dogRepository.save(oldDog);
        return ResponseEntity.ok("Dog was updated successfully.").toString();
    }
}
