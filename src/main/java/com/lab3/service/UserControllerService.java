package com.lab3.service;

import com.lab3.converter.UserConverter;
import com.lab3.dto.PatientData;
import com.lab3.dto.UserDTO;
import com.lab3.dto.UserInfo;
import com.lab3.service.data.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserControllerService {
    private UserService userService;
    private UserConverter userConverter;

    UserControllerService(UserService userService, UserConverter userConverter) {
        this.userService = userService;
        this.userConverter = userConverter;
    }

    public List<PatientData> findAllSickPatients() {
        return userConverter.toListPatientData(userService.findAllSickPatients());
    }

    public List<PatientData> findNurseTreatablePatients() {
        return userConverter.toListPatientData(userService.findNurseTreatablePatients());
    }

    public List<PatientData> findAllTreatablePatients() {
        return userConverter.toListPatientData(userService.findAllTreatablePatients());
    }

    public UserInfo findUserByLogin(String login) {
        return userConverter.convertToInfo(userService.findUserByLogin(login));
    }

    public PatientData getPatient(Long id) {
        return userConverter.toPatientData(userService.getPatientById(id));
    }

    public UserDTO getNurse(Long id) {
        return userConverter.convertToDto(userService.getNurseById(id));
    }

    public UserDTO getDoctor(Long id) {
        return userConverter.convertToDto(userService.getDoctorById(id));
    }

    public PatientData setSickPatient(Long id) {
        return userConverter.toPatientData(userService.setSickPatient(id));
    }

    public PatientData diagnosePatient(Long id, String diagnose) {
        return userConverter.toPatientData(userService.diagnosePatient(id, diagnose));
    }

    public PatientData treatPatient(Long id) {
        return userConverter.toPatientData(userService.treatPatient(id));
    }
}
