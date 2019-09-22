package com.patrycja.pound.services;

import com.patrycja.pound.models.domain.Animal;
import com.patrycja.pound.models.domain.Dog;
import com.patrycja.pound.models.domain.Zookeeper;
import com.patrycja.pound.models.dto.ZookeeperDTO;
import com.patrycja.pound.repository.ZookeeperRepository;
import com.patrycja.pound.services.mappers.ZookeeperMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ZookeeperServiceTest {

    @Mock
    private ZookeeperRepository zookeeperRepository;
    @Mock
    private ZookeeperMapper zookeeperMapper;

    private ZookeeperService zookeeperService;

    @Before
    public void setUp() {
        zookeeperService = new ZookeeperService(zookeeperRepository, zookeeperMapper);
    }

    @Test
    public void getZookeepersShouldReturnAllZookeepers() {
        List<Zookeeper> zookeepers = new ArrayList<>();
        Zookeeper zookeeper = getZookeeper();
        zookeepers.add(zookeeper);
        ZookeeperDTO zookeeperDTO = getZookeeperDTO();

        when(zookeeperRepository.findAll()).thenReturn(zookeepers);
        when(zookeeperMapper.map(zookeeper)).thenReturn(zookeeperDTO);
        List<ZookeeperDTO> zookeepersList = zookeeperService.getZookeepers();
        ZookeeperDTO actual = zookeepersList.get(0);

        assertThat(zookeepersList.size()).isEqualTo(1);
        assertThat(actual).isNotNull();
    }

    @Test
    public void updateZookeeperShouldChangeZookeeperData() {
        Zookeeper zookeeper = getZookeeper();
        ZookeeperDTO zookeeperDTO = getZookeeperDTOWithSurnamePerkins();
        int id = 1;

        when(zookeeperRepository.getOne(id)).thenReturn(zookeeper);
        zookeeperService.updateZookeeper(id, zookeeperDTO);

        verify(zookeeperRepository).save(zookeeper);
        assertThat(zookeeper.getSurname()).isEqualTo(zookeeperDTO.getSurname());
    }

    @Test
    public void addZookeeperShouldAddZookeeperToRepository() {
        ZookeeperDTO zookeeperDTO = getZookeeperDTO();
        Zookeeper zookeeper = getZookeeper();

        when(zookeeperMapper.map(zookeeperDTO)).thenReturn(zookeeper);
        zookeeperService.addZookeeper(zookeeperDTO);

        verify(zookeeperRepository).save(zookeeper);
    }

    @Test
    public void deleteAnimalFromZookeeperShouldDeleteAnimalFromZookeeperList() {
        List<Zookeeper> zookeepers = new ArrayList<>();
        List<Animal> animals = getAnimalsList();
        Zookeeper zookeeper = getZookeeper();
        zookeeper.setAnimals(animals);
        zookeepers.add(zookeeper);
        Animal animal = animals.get(0);

        when(zookeeperRepository.findAll()).thenReturn(zookeepers);
        zookeeperService.deleteAnimalFromZookeeper(animal);

        assertThat(zookeeper.getAnimals().size()).isEqualTo(0);
    }

    @Test
    public void saveAnimalToZookeeperShouldAddAnimalToZookeeperListOfAnimals() {
        Zookeeper zookeeper = getZookeeper();
        List<Animal> animals = new ArrayList<>();
        zookeeper.setAnimals(animals);
        Dog dog = getAnimalWithIdOneNumberOfToothSixNamePimpekAndAgeFour();
        Animal actual = zookeeper.getAnimals().get(0);

        zookeeperService.saveAnimalToZookeeper(dog, zookeeper);

        verify(zookeeperRepository).save(zookeeper);
        assertThat(actual).isEqualTo(dog);
    }

    @Test
    public void findFreeZookeeperShouldReturnZookeeperWithAnimalListLessThanTen() {
        List<Zookeeper> zookeeperList = new ArrayList<>();
        Zookeeper zookeeper = getZookeeper();
        Animal animal = getAnimalWithIdOneNumberOfToothSixNamePimpekAndAgeFour();
        zookeeper.setAnimals(Collections.singletonList(animal));
        zookeeperList.add(zookeeper);

        when(zookeeperRepository.findAll()).thenReturn(zookeeperList);
        Zookeeper freeZookeeper = zookeeperService.findFreeZookeeper();

        assertThat(freeZookeeper).isNotNull();
    }

    private Zookeeper getZookeeper() {
        return new Zookeeper();
    }

    private List<Animal> getAnimalsList() {
        List<Animal> animals = new ArrayList<>();
        Animal animal = new Animal();
        animals.add(animal);
        return animals;
    }

    private ZookeeperDTO getZookeeperDTO() {
        return new ZookeeperDTO();
    }

    private ZookeeperDTO getZookeeperDTOWithSurnamePerkins() {
        ZookeeperDTO zookeeperDTO = getZookeeperDTO();
        zookeeperDTO.setSurname("Perkins");
        return zookeeperDTO;
    }

    private Dog getAnimalWithIdOneNumberOfToothSixNamePimpekAndAgeFour() {
        Dog dog = new Dog();
        dog.setId(1);
        dog.setNumberOfTooth(6);
        dog.setName("Pimpek");
        dog.setAge(4);
        return dog;
    }
}