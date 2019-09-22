package com.patrycja.pound.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patrycja.pound.enums.CatColor;
import com.patrycja.pound.models.domain.Cat;
import com.patrycja.pound.models.domain.Zookeeper;
import com.patrycja.pound.models.dto.CatDTO;
import com.patrycja.pound.services.CatService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class CatResourceTest {

    private MockMvc mockMvc;
    @Mock
    private CatService catService;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(new CatResource(catService)).build();
    }

    @Test
    public void getAllCatsSorted() throws Exception {
        List<CatDTO> catDTOList = new ArrayList<>();
        CatDTO catDTO = getCatDTO();
        catDTOList.add(catDTO);
        when(catService.getAllCats("sort")).thenReturn(catDTOList);
        String json = "[{\"id\":2,\"name\":\"Ania\",\"age\":77,\"color\":\"BLACK\",\"zookeeperName\":\"Maja\"}]";
        mockMvc.perform(get("/cats")
                .param("sort", "sort")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(json)));
    }

    @Test
    public void getCatsWithColorBlack() throws Exception {
        List<CatDTO> catDTOList = new ArrayList<>();
        CatDTO catDTOBlack = getCatDTO();
        catDTOList.add(catDTOBlack);
        String color = "BLACK";
        when(catService.getCatByColor(color)).thenReturn(catDTOList);
        String json = "[{\"id\":2,\"name\":\"Ania\",\"age\":77,\"color\":\"BLACK\",\"zookeeperName\":\"Maja\"}]";
        mockMvc.perform(get("/cats/colors/{color}",color).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(json)));
    }

    @Test
    public void postCatShouldCreateNewCat() throws Exception {
        mockMvc.perform(post("/cats")
        .content(asJsonString(new Cat("Masza", 21, CatColor.PINK)))
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteCatShouldDeleteCatWithIdOne() throws Exception {
        mockMvc.perform(delete("/cats/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateCatShouldUpdateCatData() throws Exception {
        mockMvc.perform(put("/cats/{id}", 1)
                .content(asJsonString(new Cat("Kuba", 22, CatColor.BLACK)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private CatDTO getCatDTO() {
        return CatDTO.builder()
                .id(2)
                .name("Ania")
                .color(CatColor.BLACK)
                .age(77)
                .zookeeperName("Maja")
                .build();
    }

    private CatDTO getCatDTOWithPinkColor() {
        return CatDTO.builder()
                .id(2)
                .name("Ania")
                .color(CatColor.PINK)
                .age(77)
                .zookeeperName("Maja")
                .build();
    }

    private Zookeeper getZookeeperMaja() {
        return Zookeeper.builder()
                .name("Maja")
                .build();
    }


}