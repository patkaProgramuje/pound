package com.patrycja.pound.services;

import com.patrycja.pound.models.domain.Animal;
import com.patrycja.pound.models.dto.AnimalDTO;
import com.patrycja.pound.repository.AnimalRepository;
import com.patrycja.pound.services.mappers.AnimalMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AnimalServiceTest {

    @Mock
    private AnimalMapper animalMapper;
    @Mock
    private AnimalRepository animalRepository;

    private AnimalService animalService;

    @Before
    public void setUp() {
        animalService = new AnimalService(animalMapper, animalRepository);
    }

    @Test
    public void getAnimalsShouldReturnListOfAnimals() {
        List<Animal> animalList = new ArrayList<>();
        Animal animal = new Animal();
        animalList.add(animal);
        AnimalDTO animalDTO = new AnimalDTO();

        when(animalRepository.findAll()).thenReturn(animalList);
        when(animalMapper.map(animal)).thenReturn(animalDTO);
        List<AnimalDTO> animals = animalService.getAnimals();

        assertThat(animals.size()).isEqualTo(1);
        assertThat(animals.get(0)).isEqualTo(animalDTO);
    }

}