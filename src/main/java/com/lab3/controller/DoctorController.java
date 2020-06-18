package com.lab3.controller;

import com.lab3.dto.Diagnose;
import com.lab3.dto.PatientData;
import com.lab3.dto.UserDTO;
import com.lab3.service.UserControllerService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
public class DoctorController {
    private UserControllerService userControllerService;

    public DoctorController(UserControllerService userControllerService){
        this.userControllerService = userControllerService;
    }

    @GetMapping(value = "/doctor/{id}")
    public UserDTO getUser(@PathVariable("id") Long id) {
        return userControllerService.getDoctor(id);
    }

    @GetMapping(value = "/doctor/sick_patients")
    public List<PatientData> sickPatients() {
        return userControllerService.findAllSickPatients();
    }

    @GetMapping(value = "/doctor/treatable_patients")
    public List<PatientData> treatablePatients() {
        return userControllerService.findAllTreatablePatients();
    }

    @PostMapping(value = "/doctor/treat_patient/{patient_id}")
    public PatientData treatPatient(@PathVariable("patient_id") Long id) {
        return userControllerService.treatPatient(id);
    }

    @PostMapping(value = "/doctor/diagnose_patient/{patient_id}")
    public PatientData diagnosePatient(@PathVariable("patient_id") Long id, @Valid @RequestBody Diagnose diagnose) {
        return userControllerService.diagnosePatient(id, diagnose.getDiagnose());
    }
}
