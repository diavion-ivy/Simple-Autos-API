package com.galvanize.simple_autos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AutosServiceTest {

    private AutosService autosService;

    @Mock
    AutosRepository autosRepository;

    @BeforeEach
    void setUp() {
        autosService = new AutosService(autosRepository);
    }

    @Test
    void getAutos_noArg_returnslist() {
        AutosList autosList = autosService.getAutos();
        assertThat(autosList).isNotNull();
    }

    @Test
    void getAutos_search_returnsList() {
        Automobile automobile = new Automobile(1967, "Mustang", "Ford","AA88CC");
        automobile.setColor("RED");
        when(autosRepository.findColorContainsMakeContains(anyString(), anyString()))
                .thenReturn(Arrays.asList(automobile));
        AutosList autosList = autosService.getAutos("RED", "Ford");
        assertThat(autosList).isNotNull();
    }

    @Test
    void addAuto_valid_returnsAuto() {
        Automobile automobile = new Automobile(1967, "Mustang", "Ford","AA88CC");
        automobile.setColor("RED");
        when(autosRepository.save(any(Automobile.class)))
                .thenReturn(automobile);
        Automobile auto = autosService.addAuto(automobile);
        assertThat(auto).isNotNull();
        assertThat(auto.getMake()).isEqualTo("Ford");
    }

    @Test
    void getAuto_withVin_returnsAuto() {
        Automobile automobile = new Automobile(1967, "Mustang", "Ford","AA88CC");
        automobile.setColor("RED");
        when(autosRepository.findByVin(anyString()))
                .thenReturn(Optional.of(automobile));
        Automobile auto = autosService.getAuto(automobile.getVin());
        assertThat(auto).isNotNull();
        assertThat(auto.getVin()).isEqualTo(automobile.getVin());
    }

    @Test
    void updateAuto() {
        Automobile automobile = new Automobile(1967, "Mustang", "Ford","AA88CC");
        automobile.setColor("RED");
        when(autosRepository.findByVin(anyString()))
                .thenReturn(Optional.of(automobile));
        when(autosRepository.save(any(Automobile.class))).thenReturn(automobile);
        Automobile auto = autosService.updateAuto(automobile.getVin(), "PINK", "Ivy");
        assertThat(auto).isNotNull();
        assertThat(auto.getVin()).isEqualTo(automobile.getVin());
    }

    @Test
    void deleteAuto_byVin() {
        Automobile automobile = new Automobile(1967, "Mustang", "Ford","AA88CC");
        automobile.setColor("RED");
        when(autosRepository.findByVin(anyString())).thenReturn(Optional.of(automobile));

        autosService.deleteAuto(automobile.getVin());
        verify(autosRepository).delete(any(Automobile.class));
    }


    @Test
    void deleteAuto_byVin_notExists() {
        when(autosRepository.findByVin(anyString())).thenReturn(Optional.empty());
        assertThatExceptionOfType(AutoNotFoundException.class)
                .isThrownBy(() -> {
                    autosService.deleteAuto("NOVINHERE");
                });

    }
}