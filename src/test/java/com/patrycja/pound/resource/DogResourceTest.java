package com.patrycja.pound.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patrycja.pound.models.domain.Dog;
import com.patrycja.pound.models.dto.DogDTO;
import com.patrycja.pound.services.DogService;
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
public class DogResourceTest {

    @Mock
    private DogService dogService;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new DogResource(dogService)).build();
    }

    @Test
    public void deleteDog() throws Exception {
        mockMvc.perform(delete("/dogs/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void addDogShouldCreateNewDog() throws Exception {
        mockMvc.perform(post("/dogs")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(getDogDTO())))
                .andExpect(status().isOk());
    }

    @Test
    public void getDogsShouldReturnAllDogs() throws Exception {
        List<DogDTO> dogDTOList = new ArrayList<>();
        DogDTO dogDTO = getDogDTO();
        dogDTOList.add(dogDTO);

        when(dogService.getDogs()).thenReturn(dogDTOList);

        mockMvc.perform(get("/dogs")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(asJsonString(getDogDTO()))));
    }

    @Test
    public void updateDogShouldUpdateDogData() throws Exception {
        mockMvc.perform(put("/dogs/{id}", 1)
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(getDog())))
                .andExpect(status().isOk());
    }

    private DogDTO getDogDTO() {
        return DogDTO.builder()
                .id(1)
                .age(22)
                .name("Masza")
                .numberOfTooth(3)
                .zookeeperName("Maja")
                .build();
    }

    private Dog getDog() {
        return Dog.dogBuilder()
                .age(2)
                .name("Kuba")
                .numberOfTooth(5)
                .build();
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}