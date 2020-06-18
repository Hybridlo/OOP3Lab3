package com.lab3.service.data;

import com.lab3.entity.Data;
import com.lab3.entity.User;
import com.lab3.exceptions.UserIsAlreadySickException;
import com.lab3.exceptions.UserIsNotExpectedTypeException;
import com.lab3.exceptions.UserNotFoundException;
import com.lab3.repository.DataRepository;
import com.lab3.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private DataRepository dataRepository;

    @Test(expected = UserNotFoundException.class)
    public void testGetUserExceptionNotFound() {
        UserService userService = new UserService(userRepository, dataRepository);
        Long id = 5L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        userService.getPatientById(id);
    }

    @Test(expected = UserIsNotExpectedTypeException.class)
    public void testGetUserWrongType() {
        UserService userService = new UserService(userRepository, dataRepository);

        User user = new User();
        Data data = new Data();
        user.setData(data);
        data.setType("doctor");

        Long id = 5L;

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        userService.getPatientById(id);
    }

    @Test(expected = UserIsAlreadySickException.class)
    public void testSetSickAlreadySick() {
        UserService userService = new UserService(userRepository, dataRepository);

        User user = new User();
        Data data = new Data();
        user.setData(data);
        data.setIsSick(true);
        data.setType("patient");

        Long id = 5L;

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        userService.setSickPatient(id);
    }
}
