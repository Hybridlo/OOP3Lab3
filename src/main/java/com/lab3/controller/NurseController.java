package com.lab3.controller;

import com.lab3.dto.PatientData;
import com.lab3.dto.UserDTO;
import com.lab3.service.UserControllerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class NurseController {
    private UserControllerService userControllerService;

    public NurseController(UserControllerService userControllerService){
        this.userControllerService = userControllerService;
    }

    @GetMapping(value = "/nurse/{id}")
    public UserDTO getUser(@PathVariable("id") Long id) {
        return userControllerService.getNurse(id);
    }

    @GetMapping(value = "/nurse/treatable_patients")
    public List<PatientData> treatablePatients() {
        return userControllerService.findNurseTreatablePatients();
    }

    @PostMapping(value = "/nurse/treat_patient/{patient_id}")
    public PatientData treatPatient(@PathVariable("patient_id") Long id) {
        return userControllerService.treatPatient(id);
    }
}
