package com.galvanize.simple_autos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AutosController.class)
public class AutoControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AutosService autosService;


    //GET: /api/autos
    // returns list of all autos
    @Test
    void getAutos_noParam_exist_returnsAutoslists() throws Exception {
        List<Automobile> automobiles = new ArrayList<>();
        for (int i = 0; i < 5 ; i++) {
            automobiles.add(new Automobile(1900+i, "Ford", "Mustang","AA88"+i));
        }
        when(autosService.getAutos()).thenReturn(new AutosList(automobiles));
        mockMvc.perform(get("/api/autos"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.automobiles", hasSize(5)));
    }


    // returns 204 no autos found
    @Test
    void getAutos_noParams_none_returnsNoContent() throws Exception {
        List<Automobile> automobiles = new ArrayList<>();
        when(autosService.getAutos()).thenReturn(new AutosList(automobiles));
        mockMvc.perform(get("/api/autos"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    // /api/autos?color=RED returns red cars

    // /api/autos?make=Ford returns ford cars
    // /api/autos?color=GREEN&make=Ford returns red cars

    //POST: /api/autos
    // returns created automobiles
    // returns error messages due to bad request (400)

    //GET: /api/autos{vin}
    // return requested automobile
    // returns NoContent(204) Auto not found

    //PATCH: /api/autos{vin}
    // returns patched automobile
    // return NoContent(204) auto not found
    // returns bad request (400)

    //DELETE: /api/autos{vin}
    // returns 202 delete request accepted
    // returns NoContent(204) vehicle not found
}
