package com.dandobai.user_handler.services;

import com.dandobai.user_handler.dtos.UserDTO;
import com.dandobai.user_handler.dtos.UserRegistrationDTO;
import com.dandobai.user_handler.dtos.authentication.AuthenticationRequest;
import com.dandobai.user_handler.dtos.authentication.AuthenticationResponse;
import com.dandobai.user_handler.models.User;
import com.dandobai.user_handler.repositories.UserRepository;
import com.dandobai.user_handler.utils.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public UserServiceImpl(CustomUserDetailsService userDetailsService, ModelMapper modelMapper, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public List<UserDTO> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDTO> findUserById(Long id) {
        return userRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        return convertToDTO(userRepository.save(user));
    }

    @Override
    public Optional<UserDTO> updateUser(Long id, UserDTO userDTO) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setName(userDTO.getName());
            existingUser.setEmail(userDTO.getEmail());
            return convertToDTO(userRepository.save(existingUser));
        });
    }

    @Override
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public AuthenticationResponse validateAndLogin(AuthenticationRequest authenticationRequest) {
        // Authenticate the user with the authentication manager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword())
        );

        // Once authenticated, load user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        // Generate JWT token
        String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return new AuthenticationResponse(jwt);
    }


    @Override
    public AuthenticationResponse validateAndSave(UserRegistrationDTO userRegistrationDTO) {
        if (userRepository.existsByEmail(userRegistrationDTO.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User user = convertToUser(userRegistrationDTO);
        userRepository.save(user);

        // Generate JWT token using email (instead of UserDetails)
        String token = jwtUtil.generateToken(userRegistrationDTO.getEmail());
        return new AuthenticationResponse(token);
    }

    @Override
    public Double getAverageAge() {
        return userRepository.findAverageAge();
    }

    @Override
    public List<UserDTO> getUsersBetween18And40() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .filter(user -> {
                    int age = LocalDate.now().getYear() - user.getBirthday().getYear();
                    return age >= 18 && age <= 40;
                }).map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail());
    }

    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        return user;
    }

    private User convertToUser(UserRegistrationDTO userRegistrationDTO) {
        User user = modelMapper.map(userRegistrationDTO, User.class);
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        user.setRole("USER");
        user.setBirthday(LocalDate.parse(userRegistrationDTO.getBirthday()));
        return user;
    }
}
