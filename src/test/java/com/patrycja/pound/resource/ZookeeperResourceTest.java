package com.patrycja.pound.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patrycja.pound.models.dto.ZookeeperDTO;
import com.patrycja.pound.services.ZookeeperService;
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
public class ZookeeperResourceTest {

    @Mock
    private ZookeeperService zookeeperService;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ZookeeperResource(zookeeperService)).build();
    }

    @Test
    public void addZookeeperShouldCreateNewZookeeper() throws Exception {
        mockMvc.perform(post("/zookeepers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getZookeeperDTO())))
                .andExpect(status().isOk());
    }

    @Test
    public void getZookeepersShouldReturnListOfAllZookeepers() throws Exception {
        List<ZookeeperDTO> zookeeperList = new ArrayList<>();
        ZookeeperDTO zookeeperDTO = getZookeeperDTO();
        zookeeperList.add(zookeeperDTO);

        when(zookeeperService.getZookeepers()).thenReturn(zookeeperList);

        mockMvc.perform(get("/zookeepers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(asJsonString(getZookeeperDTO()))));
    }

    @Test
    public void updateZookeeperShouldUpdateZookeeperData() throws Exception {
        mockMvc.perform(put("/zookeepers/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getZookeeperDTO())))
                .andExpect(status().isOk());
    }

    private ZookeeperDTO getZookeeperDTO() {
        return ZookeeperDTO.builder()
                .id(1)
                .name("Jan")
                .surname("Kowalski")
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