package com.lab3.repository;

import com.lab3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findUsersByData_TypeAndData_IsSickTrueAndData_TreatmentIsNull(String type);

    List<User> findUsersByData_TypeAndData_TreatmentIsNotNull(String type);

    List<User> findUsersByData_TypeAndData_TreatmentIsNotNullAndData_TreatmentNot(String type, String notTreatment);

    Optional<User> findByLogin(String login);
}