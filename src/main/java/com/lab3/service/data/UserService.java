package com.lab3.service.data;

import com.lab3.entity.Data;
import com.lab3.entity.User;
import com.lab3.exceptions.UserIsAlreadySickException;
import com.lab3.exceptions.UserIsNotExpectedTypeException;
import com.lab3.exceptions.UserNotFoundException;
import com.lab3.repository.DataRepository;
import com.lab3.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {
    private UserRepository userRepository;
    private DataRepository dataRepository;

    public UserService(UserRepository userRepository, DataRepository dataRepository) {
        this.userRepository = userRepository;
        this.dataRepository = dataRepository;
    }

    public List<User> findAllSickPatients() {
        return userRepository.findUsersByData_TypeAndData_IsSickTrueAndData_TreatmentIsNull(Data.PATIENT_TYPE);
    }

    public List<User> findNurseTreatablePatients() {
        return userRepository
                .findUsersByData_TypeAndData_TreatmentIsNotNullAndData_TreatmentNot(Data.PATIENT_TYPE,
                        Data.TREATMENTS[Data.ONLY_DOC_TREATMENT]);
    }

    public List<User> findAllTreatablePatients() {
        return userRepository.findUsersByData_TypeAndData_TreatmentIsNotNull(Data.PATIENT_TYPE);
    }

    public User getPatientById(Long id) {
        Optional<User> user = userRepository.findById(id);

        User u = user.orElseThrow(UserNotFoundException::new);

        if(!u.getData().getType().equals(Data.PATIENT_TYPE)) {
            throw new UserIsNotExpectedTypeException();
        }

        return u;
    }

    public User getNurseById(Long id) {
        Optional<User> user = userRepository.findById(id);

        User u = user.orElseThrow(UserNotFoundException::new);

        if(!u.getData().getType().equals(Data.NURSE_TYPE)) {
            throw new UserIsNotExpectedTypeException();
        }

        return u;
    }

    public User getDoctorById(Long id) {
        Optional<User> user = userRepository.findById(id);

        User u = user.orElseThrow(UserNotFoundException::new);

        if(!u.getData().getType().equals(Data.DOCTOR_TYPE)) {
            throw new UserIsNotExpectedTypeException();
        }

        return u;
    }

    @Transactional
    public User setSickPatient(Long id) {
        Optional<User> user = userRepository.findById(id);

        User u = user.orElseThrow(UserNotFoundException::new);

        if(!u.getData().getType().equals(Data.PATIENT_TYPE)) {
            throw new UserIsNotExpectedTypeException();
        }

        if(u.getData().getIsSick()) {
            throw new UserIsAlreadySickException();
        }

        Data data = u.getData();

        data.setIsSick(true);

        dataRepository.save(data);

        return userRepository.findByLogin(u.getLogin())
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public User diagnosePatient(Long id, String diagnose) {
        Optional<User> user = userRepository.findById(id);

        User u = user.orElseThrow(UserNotFoundException::new);

        if(!u.getData().getType().equals(Data.PATIENT_TYPE)) {
            throw new UserIsNotExpectedTypeException();
        }

        Random random = new Random();

        Data data = u.getData();

        data.setSickness(diagnose);
        data.setTreatment(Data.TREATMENTS[random.nextInt(3)]);

        dataRepository.save(data);

        return userRepository.findByLogin(u.getLogin())
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public User treatPatient(Long id) {
        Optional<User> user = userRepository.findById(id);

        User u = user.orElseThrow(UserNotFoundException::new);

        if(!u.getData().getType().equals(Data.PATIENT_TYPE)) {
            throw new UserIsNotExpectedTypeException();
        }

        Data data = u.getData();

        data.setTreatment(null);

        String sicknessHistory = data.getSicknessHistory();

        if (sicknessHistory == null) {
            sicknessHistory = data.getSickness();
        } else {
            sicknessHistory += ", " + data.getSickness();
        }

        data.setSicknessHistory(sicknessHistory);
        data.setSickness(null);
        data.setIsSick(false);

        dataRepository.save(data);

        return userRepository.findByLogin(u.getLogin())
                .orElseThrow(UserNotFoundException::new);
    }

    public User findUserByLogin(String login) {
        Optional<User> u = userRepository.findByLogin(login);

        return u.orElseThrow(UserNotFoundException::new);
    }
}
