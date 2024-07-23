package com.galvanize.simple_autos;

import org.springframework.stereotype.Service;

@Service
public class AutosService {

    AutosRepository autosRepository;

    public AutosService(AutosRepository autosRepository) {
        this.autosRepository = autosRepository;
    }

    public AutosList getAutos() {

        return new AutosList(autosRepository.findAll());
    };

    public AutosList getAutos(String color, String make) {
        return null;
    }


    public Automobile addAuto(Automobile auto) {
        return null;
    }

    public Automobile getAuto(String vin) {
        return null;
    }

    public Automobile updateAuto(String vin, String color, String owner) {
        /*
        1.find automobile by vin
            - if not found throw auto not found exception
        2. change the color and owner
        3. save that updated automobile
        4. return updated automobile
         */
        return null;
    }

    public void deleteAuto(String vin) {
    }
}
