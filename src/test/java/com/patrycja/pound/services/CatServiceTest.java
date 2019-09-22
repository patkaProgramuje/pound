package com.patrycja.pound.services;

import com.patrycja.pound.enums.CatColor;
import com.patrycja.pound.models.domain.Cat;
import com.patrycja.pound.models.domain.Zookeeper;
import com.patrycja.pound.models.dto.CatDTO;
import com.patrycja.pound.repository.CatRepository;
import com.patrycja.pound.services.mappers.CatMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CatServiceTest {

    @Mock
    private CatRepository catRepository;
    @Mock
    private CatMapper catMapper;
    @Mock
    private ZookeeperService zookeeperService;

    private CatService catService;

    @Before
    public void setUp() {
        catService = new CatService(catRepository, catMapper, zookeeperService);
    }

    @Test
    public void getCatByIdShouldReturnCat() {
        int id = 1;
        Cat cat = getCat();

        when(catRepository.findById(id)).thenReturn(Optional.of(cat));
        Cat catById = catService.getCatById(id);

        assertThat(catById).isEqualTo(cat);
    }

    @Test
    public void updateCatShouldUpdateCatInfo() {
        CatDTO catDTO = createCatDTOWithNamePimpekAgeTwelveAndColorGrey();
        int id = 1;
        Cat cat = getCat();
        cat.setAge(catDTO.getAge());
        cat.setName(catDTO.getName());
        cat.setColor(catDTO.getColor());

        when(catRepository.findById(id)).thenReturn(Optional.of(cat));
        catService.updateCat(id, catDTO);

        verify(catRepository).save(cat);
    }

    @Test
    public void getCatNameShouldReturnCatName() {
        int id = 1;
        Cat cat = getCatWithIdOneAndNamePimpek();

        when(catRepository.findById(id)).thenReturn(Optional.of(cat));
        String foundCatName = catService.getCatName(id);

        assertThat(foundCatName).isNotEqualTo(null);
        assertThat(foundCatName).isEqualTo(cat.getName());
    }

    @Test
    public void addCatShouldCreateNewCat() {
        CatDTO catDTO = createCatDTOWithNamePimpekAgeTwelveAndColorGrey();
        Cat cat = getCat();
        Zookeeper zookeeper = new Zookeeper();
        cat.setZookeeper(zookeeper);
        catService.addCat(catDTO);

        when(catMapper.map(catDTO)).thenReturn(cat);
        when(zookeeperService.findFreeZookeeper()).thenReturn(zookeeper);

        verify(catRepository).save(cat);
        verify(zookeeperService).saveAnimalToZookeeper(cat, zookeeper);
    }

    @Test
    public void deleteCatShouldDeleteCatFromRepositoryAndZookeeperListOfAnimals() {
        int id = 1;
        Cat cat = getCatWithIdOne();

        when(catRepository.getOne(id)).thenReturn(cat);
        catService.deleteCat(id);

        verify(zookeeperService).deleteAnimalFromZookeeper(cat);
        verify(catRepository).delete(cat);
    }

    @Test
    public void getCatByColorShouldReturnListWithCatThisColor() {
        List<Cat> list = new ArrayList<>();
        String pink = "GREY";
        CatDTO catDTO = createCatDTOWithNamePimpekAgeTwelveAndColorGrey();
        int expected = 1;
        Cat cat = getCat();
        list.add(cat);

        when(catRepository.findAll()).thenReturn(list);
        when(catMapper.map(cat)).thenReturn(catDTO);
        List<CatDTO> catByColor = catService.getCatByColor(pink);

        assertThat(catByColor.size()).isEqualTo(expected);
        assertThat(catByColor.get(0).getColor().toString()).isEqualTo(pink);
    }

    private Cat getCat() {
        return new Cat();
    }

    private Cat getCatWithIdOne() {
        Cat cat = getCat();
        cat.setId(1);
        return cat;
    }

    private Cat getCatWithIdOneAndNamePimpek() {
        Cat catWithIdOne = getCatWithIdOne();
        catWithIdOne.setName("Pimpek");
        return catWithIdOne;
    }

    private CatDTO createCatDTOWithNamePimpekAgeTwelveAndColorGrey() {
        CatDTO catDTO = new CatDTO();
        catDTO.setName("Pimpek");
        catDTO.setAge(12);
        catDTO.setColor(CatColor.GREY);
        return catDTO;
    }
}
