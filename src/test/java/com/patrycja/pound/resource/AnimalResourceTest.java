package com.patrycja.pound.resource;

import com.patrycja.pound.models.dto.AnimalDTO;
import com.patrycja.pound.models.dto.CatDTO;
import com.patrycja.pound.services.AnimalService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest
public class AnimalResourceTest {

    private MockMvc mockMvc;
    @Mock
    private AnimalService animalService;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(new AnimalResource(animalService)).build();
    }

    @Test
    public void getAllAnimalsShouldReturnListAllAnimals() throws Exception {
        List<AnimalDTO> animalList = new ArrayList<>();
        AnimalDTO animalDTO = getAnimalDTO();
        animalList.add(animalDTO);
        when(animalService.getAnimals()).thenReturn(animalList);
        String json = "[{\"id\":2,\"name\":\"Ania\",\"age\":77,\"type\":\"Cat\",\"zookeeper\":\"Maja\"}] ";
        this.mockMvc.perform(get("/animals")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(json)));
    }

    @Test
    public void getAllAnimalsShouldReturn400ClientError() throws Exception {
        this.mockMvc.perform(get("/animal")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    private AnimalDTO getAnimalDTO() {
        return AnimalDTO.builder()
                .id(2)
                .name("Ania")
                .zookeeper("Maja")
                .type("Cat")
                .age(77)
                .build();
    }
}