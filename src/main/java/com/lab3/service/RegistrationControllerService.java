package com.lab3.service;

import com.lab3.converter.UserConverter;
import com.lab3.dto.UserDTO;
import com.lab3.entity.User;
import com.lab3.service.data.RegistrationService;
import org.springframework.stereotype.Service;

@Service
public class RegistrationControllerService {
    private RegistrationService registrationService;
    private UserConverter userConverter;

    RegistrationControllerService(UserConverter userConverter, RegistrationService registrationService){
        this.userConverter = userConverter;
        this.registrationService = registrationService;
    }

    public UserDTO save(UserDTO userDTO) {
        User currentUser = userConverter.convertToEntity(userDTO);
        UserDTO savedUserDto = userConverter.convertToDto(registrationService.save(currentUser));

        savedUserDto.setPassword(userDTO.getPassword());

        return savedUserDto;
    }
}
