package com.lab3.service.data;

import com.lab3.entity.User;
import com.lab3.exceptions.UserAlreadyExistsException;
import com.lab3.repository.DataRepository;
import com.lab3.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class RegistrationService {
    private UserRepository userRepository;
    private DataRepository dataRepository;

    public RegistrationService(UserRepository userRepository, DataRepository dataRepository) {
        this.userRepository = userRepository;
        this.dataRepository = dataRepository;
    }

    @Transactional
    public User save(User currentUser) {
        Optional<User> oldUser = userRepository.findByLogin(currentUser.getLogin());

        oldUser.ifPresent(entity -> {throw new UserAlreadyExistsException();});

        currentUser.setLogin(currentUser.getLogin().toLowerCase());

        User savedUser = userRepository.save(currentUser);

        dataRepository.save(currentUser.getData());

        return savedUser;
    }
}
