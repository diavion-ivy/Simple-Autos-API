package com.galvanize.simple_autos;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AutosController {

    AutosService autosService;

    public AutosController(AutosService autosService) {
        this.autosService = autosService;
    }

    @GetMapping("/api/autos")
    public ResponseEntity<AutosList> getAutos(@RequestParam(required = false) String color, @RequestParam(required = false) String make) {

        AutosList autosList;
        if (color == null && make == null) {
            autosList = autosService.getAutos();
        } else {
            autosList = autosService.getAutos(color, make);
        }
        return autosList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(autosList);
    }

    @PostMapping("/api/autos")
    public Automobile addAutos(@RequestBody Automobile auto) {
        return autosService.addAuto(auto);
    }

    @GetMapping("/api/autos/{vin}")
    public ResponseEntity<Automobile> getAuto(@PathVariable String vin) {
        Automobile auto = autosService.getAuto(vin);
//        return auto == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(auto);
        return ResponseEntity.ok(auto);
    }
    @PatchMapping("/api/autos/{vin}")
    public Automobile updateAuto(@PathVariable String vin, @RequestBody UpdateOwnerRequest update) {
        return autosService.updateAuto(vin, update.getColor(), update.getOwner());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void noContent(AutoNotFoundException e) {}

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void invalidAutoExceptionHandler(InvalidAutoException e){
    }

}
