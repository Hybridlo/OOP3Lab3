package com.lab3.converter;

import com.lab3.dto.PatientData;
import com.lab3.dto.UserCredentials;
import com.lab3.dto.UserDTO;
import com.lab3.dto.UserInfo;
import com.lab3.entity.Data;
import com.lab3.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserConverter {

    public PatientData toPatientData(User user) {
        PatientData patientData = new PatientData();
        patientData.setId(user.getId());
        patientData.setName(user.getLogin());
        patientData.setIsSick(user.getData().getIsSick());
        patientData.setSickness(user.getData().getSickness());
        patientData.setSicknessHistory(user.getData().getSicknessHistory());
        patientData.setTreatment(user.getData().getTreatment());

        return patientData;
    }

    public List<PatientData> toListPatientData(List<User> users){
        return users.stream()
                .map(this::toPatientData)
                .collect(Collectors.toList());
    }

    public UserInfo convertToInfo(User user){
        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getId());
        userInfo.setLogin(user.getLogin());
        userInfo.setType(user.getData().getType());

        return userInfo;
    }

    public UserCredentials dtoToCredentials(UserDTO userDTO) {
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setLogin(userDTO.getLogin());
        userCredentials.setPassword(userDTO.getPassword());

        return userCredentials;
    }

    public UserDTO convertToDto(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin(user.getLogin());
        userDTO.setType(user.getData().getType());

        return userDTO;
    }

    public User convertToEntity(UserDTO userDTO){
        User user = new User();
        user.setLogin(userDTO.getLogin());

        Data data = new Data();
        data.setIsSick(false);
        data.setType(userDTO.getType());
        data.setUser(user);

        user.setData(data);

        return user;
    }

    public List<UserDTO> convertToListDto(List<User> users){
        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}