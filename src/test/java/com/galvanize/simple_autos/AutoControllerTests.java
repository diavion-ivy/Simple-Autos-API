package com.galvanize.simple_autos;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AutosController.class)
public class AutoControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AutosService autosService;

    ObjectMapper mapper = new ObjectMapper();

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

    // /api/autos?make=Ford returns ford cars
    @Test
    void getAutos_searchParams_makeExists_returnsAutoslists() throws Exception {
        List<Automobile> automobiles = new ArrayList<>();
        for (int i = 0; i < 5 ; i++) {
            automobiles.add(new Automobile(1900+i, "Ford", "Mustang","AA88"+i));
        }
        when(autosService.getAutos(isNull(),anyString())).thenReturn(new AutosList(automobiles));
        mockMvc.perform(get("/api/autos?make=Ford"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.automobiles", hasSize(5)));
    }

    // /api/autos?color=RED returns red cars
    @Test
    void getAutos_searchParams_colorExists_returnsAutoslists() throws Exception {
        List<Automobile> automobiles = new ArrayList<>();
        for (int i = 0; i < 5 ; i++) {
            automobiles.add(new Automobile(1900+i, "Ford", "Mustang","AA88"+i));
        }
        when(autosService.getAutos(anyString(),isNull())).thenReturn(new AutosList(automobiles));
        mockMvc.perform(get("/api/autos?color=RED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.automobiles", hasSize(5)));
    }

    // /api/autos?color=GREEN&make=Ford returns red cars
    @Test
    void getAutos_searchParams_exists_returnsAutoslists() throws Exception {
        List<Automobile> automobiles = new ArrayList<>();
        for (int i = 0; i < 5 ; i++) {
            automobiles.add(new Automobile(1900+i, "Ford", "Mustang","AA88"+i));
        }
        when(autosService.getAutos(anyString(), anyString())).thenReturn(new AutosList(automobiles));
        mockMvc.perform(get("/api/autos?color=RED&make=Ford"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.automobiles", hasSize(5)));
    }


    //POST: /api/autos
    // returns created automobiles
    @Test
    void addAuto_valid_returnAuto() throws Exception {
        Automobile automobile = new Automobile(1967, "Mustang", "Ford","AA88CC");
        when(autosService.addAuto(any(Automobile.class))).thenReturn(automobile);
        mockMvc.perform(post("/api/autos").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(automobile)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("make").value("Ford"));
    }

    // returns error messages due to bad request (400)
    @Test
    void addAuto_badRequ_returns400() throws Exception {
        when(autosService.addAuto(any(Automobile.class))).thenThrow(InvalidAutoException.class);
        String json = "{\"year\":1967,\"make\":\"Ford\",\"model\":\"Mustang\",\"color\":null,\"owner\":null,\"vin\":\"AA88CC\"}";
        mockMvc.perform(post("/api/autos").contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    //GET: /api/autos{vin}
    // return requested automobile
    @Test
    void getAuto_withVin_returnsAuto() throws Exception {
        Automobile automobile = new Automobile(1967, "Mustang", "Ford","AA88CC");
        when(autosService.getAuto(anyString())).thenReturn(automobile);
        mockMvc.perform(get("/api/autos/"+automobile.getVin()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("vin").value(automobile.getVin()));
    }

    // returns NoContent(204) Auto not found
    @Test
    void getAuto_withVin_returnsNoContent() throws Exception {
        when(autosService.getAuto(anyString())).thenThrow(AutoNotFoundException.class);
        mockMvc.perform(get("/api/autos/NOTFOUND"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }


    //PATCH: /api/autos{vin}
    // returns patched automobile
    @Test
    void updateAuto_withObject_returnsAuto() throws Exception {
        Automobile automobile = new Automobile(1967, "Mustang", "Ford","AA88CC");
        automobile.setColor("RED");
        automobile.setOwner("Ivy");
        when(autosService.updateAuto(anyString(),anyString(),anyString())).thenReturn(automobile);
        mockMvc.perform(patch("/api/autos/"+automobile.getVin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"color\":\"RED\",\"owner\":\"Ivy\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("color").value("RED"))
                .andExpect(jsonPath("owner").value("Ivy"));
    }

    // return NoContent(204) auto not found
    @Test
    void updateAuto_withObject_returnNoContent() throws Exception {
        when(autosService.updateAuto(anyString(),anyString(),anyString())).thenThrow(AutoNotFoundException.class);
        mockMvc.perform(patch("/api/autos/NOTFOUND")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"color\":\"RED\",\"owner\":\"Ivy\"}"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    // returns bad request (400)
    @Test
    void updateAuto_withObject_returnBadRequ() throws Exception {
        when(autosService.updateAuto(anyString(),anyString(),anyString())).thenThrow(InvalidAutoException.class);
        mockMvc.perform(patch("/api/autos/NOTFOUND")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"color\":\"RED\",\"owner\":\"Ivy\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    //DELETE: /api/autos{vin}
    // returns 202 delete request accepted
    @Test
    void deleteAuto_withVin_returns202() throws Exception {
        mockMvc.perform(delete("/api/autos/AA88CC"))
                .andExpect(status().isAccepted());
        verify(autosService).deleteAuto(anyString());
    }

    // returns NoContent(204) vehicle not found
    @Test
    void deleteAuto_withVin_returnsNoContent() throws Exception {
        doThrow(AutoNotFoundException.class).when(autosService).deleteAuto(anyString());
        mockMvc.perform(delete("/api/autos/AA88CC"))
                .andExpect(status().isNoContent());
    }
}
