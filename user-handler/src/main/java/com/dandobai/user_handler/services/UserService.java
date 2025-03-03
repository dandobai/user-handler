package com.dandobai.user_handler.services;

import com.dandobai.user_handler.dtos.UserDTO;
import com.dandobai.user_handler.dtos.UserRegistrationDTO;
import com.dandobai.user_handler.dtos.authentication.AuthenticationRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDTO> findAllUsers();
    Optional<UserDTO> findUserById(Long id);
    UserDTO createUser(UserDTO userDTO);
    Optional<UserDTO> updateUser(Long id, UserDTO userDTO);
    boolean deleteUser(Long id);

    Object validateAndLogin(AuthenticationRequest authenticationRequest);

    Object validateAndSave(UserRegistrationDTO userRegistrationDTO);

    Double getAverageAge();

    List<UserDTO> getUsersBetween18And40();
}
