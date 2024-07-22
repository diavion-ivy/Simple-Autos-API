package com.galvanize.simple_autos;

public class AutoControllerTests {
    //GET: /api/autos
    // returns list of all autos
    // returns 204 no autos found
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
