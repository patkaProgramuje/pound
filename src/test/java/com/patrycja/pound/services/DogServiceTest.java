package com.patrycja.pound.services;

import com.patrycja.pound.models.domain.Dog;
import com.patrycja.pound.models.domain.Zookeeper;
import com.patrycja.pound.models.dto.DogDTO;
import com.patrycja.pound.repository.DogRepository;
import com.patrycja.pound.services.mappers.DogMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DogServiceTest {

    @Mock
    private DogRepository dogRepository;
    @Mock
    private DogMapper dogMapper;
    @Mock
    private ZookeeperService zookeeperService;

    private DogService dogService;

    @Before
    public void setUp() {
        dogService = new DogService(dogRepository, dogMapper, zookeeperService);
    }

    @Test
    public void addDogShouldCreateNewDog() {
        Dog dog = getDog();
        DogDTO dogDTO = getDogDTO();
        Zookeeper zookeeper = getZookeeper();

        when(dogMapper.map(dogDTO)).thenReturn(dog);
        when(zookeeperService.findFreeZookeeper()).thenReturn(zookeeper);
        dogService.addDog(dogDTO);

        verify(dogRepository).save(dog);
        verify(zookeeperService).saveAnimalToZookeeper(dog, zookeeper);
    }

    @Test
    public void updateDogShouldUpdateDogData() {
        Dog dog = getDog();
        DogDTO dogDTO = getDogDTOWithIdOneNumberOfToothFourAgeThreeAndNamePimpek();
        int id = 1;

        when(dogRepository.getOne(id)).thenReturn(dog);
        dogService.updateDog(id, dogDTO);

        verify(dogRepository).save(dog);
        assertThat(dog.getName()).isEqualTo(dogDTO.getName());
    }

    @Test
    public void getAllDogsShouldReturnListOfDogs() {
        List<Dog> dogList = new ArrayList<>();
        Dog dog = getDog();
        DogDTO dogDTO = getDogDTO();
        dogList.add(dog);

        when(dogRepository.findAll()).thenReturn(dogList);
        when(dogMapper.map(dog)).thenReturn(dogDTO);

        assertThat(dogList.size()).isEqualTo(1);
    }

    @Test
    public void deleteDogShouldDeleteDogFromRepositoryAndZookeeperListOfAnimals() {
        Dog dog = getDog();
        int id = 1;

        when(dogRepository.getOne(id)).thenReturn(dog);
        dogService.deleteDog(id);

        verify(zookeeperService).deleteAnimalFromZookeeper(dog);
        verify(dogRepository).deleteById(id);
    }

    private DogDTO getDogDTOWithIdOneNumberOfToothFourAgeThreeAndNamePimpek() {
        DogDTO dog = getDogDTO();
        dog.setName("Pimpek");
        dog.setAge(3);
        dog.setNumberOfTooth(4);
        return dog;
    }

    private Dog getDog() {
        return new Dog();
    }

    private DogDTO getDogDTO() {
        return new DogDTO();
    }

    private Zookeeper getZookeeper() {
        return new Zookeeper();
    }
}