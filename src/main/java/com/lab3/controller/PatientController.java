package com.lab3.controller;

import com.lab3.dto.PatientData;
import com.lab3.dto.UserDTO;
import com.lab3.service.UserControllerService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class PatientController {
    private UserControllerService userControllerService;

    public PatientController(UserControllerService userControllerService){
        this.userControllerService = userControllerService;
    }

    @GetMapping(value = "/patient/{id}")
    public PatientData getUser(@PathVariable("id") Long id) {
        return userControllerService.getPatient(id);
    }

    @PostMapping(value = "/patient/{id}/setsick")
    public PatientData setSick(@PathVariable("id") Long id) {
        return userControllerService.setSickPatient(id);
    }
}
