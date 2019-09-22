package com.patrycja.pound.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patrycja.pound.enums.CatColor;
import com.patrycja.pound.models.domain.Cat;
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
        String sort = "sort";

        when(catService.getAllCats("sort")).thenReturn(catDTOList);

        mockMvc.perform(get("/cats")
                .param("sort", sort)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(asJsonString(getCatDTO()))));
    }

    @Test
    public void getCatsWithColorBlack() throws Exception {
        List<CatDTO> catDTOList = new ArrayList<>();
        CatDTO catDTOBlack = getCatDTO();
        catDTOList.add(catDTOBlack);
        String color = "BLACK";

        when(catService.getCatByColor(color)).thenReturn(catDTOList);

        mockMvc.perform(get("/cats/colors/{color}", color)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(asJsonString(getCatDTO()))));
    }

    @Test
    public void postCatShouldCreateNewCat() throws Exception {
        mockMvc.perform(post("/cats")
                .content(asJsonString(getCatMaszaAndAgeTwentyOneAndColorPink()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateCatShouldUpdateCatData() throws Exception {
        mockMvc.perform(put("/cats/{id}", 1)
                .content(asJsonString(getCatMaszaAndAgeTwentyOneAndColorPink()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteCatShouldDeleteCatWithIdOne() throws Exception {
        mockMvc.perform(delete("/cats/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
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

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Cat getCatMaszaAndAgeTwentyOneAndColorPink() {
        return new Cat("Masza", 21, CatColor.PINK);
    }
}