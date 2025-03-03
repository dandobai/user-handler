package com.dandobai.user_handler.services;

import com.dandobai.user_handler.dtos.UserDTO;
import com.dandobai.user_handler.dtos.UserRegistrationDTO;
import com.dandobai.user_handler.dtos.authentication.AuthenticationRequest;
import com.dandobai.user_handler.dtos.authentication.AuthenticationResponse;
import com.dandobai.user_handler.models.User;
import com.dandobai.user_handler.repositories.UserRepository;
import com.dandobai.user_handler.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private PasswordEncoder passwordEncoderMock;

    @Mock
    private JwtUtil jwtUtilMock;

    @Mock
    private AuthenticationManager authenticationManagerMock;

    @Mock
    private CustomUserDetailsService userDetailsServiceMock;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Setting up a mock User object for the tests
        user = new User("John Doe", "john.doe@example.com", "john_doe", "password123", "USER", LocalDate.of(1990, 1, 1));
    }

    @Test
    public void testCreateUser() {
        // Creating a UserDTO object to be passed as a request
        UserDTO userDTO = new UserDTO(1L, "John Doe", "john.doe@example.com");

        // Mock behavior: when save is called on userRepository, return the mock user object
        when(userRepositoryMock.save(any(User.class))).thenReturn(user);

        // Call the service method to create a user
        UserDTO createdUser = userService.createUser(userDTO);

        // Assertions to verify the result
        assertNotNull(createdUser);
        assertEquals(user.getId(), createdUser.getId());
        assertEquals(user.getName(), createdUser.getName());
        assertEquals(user.getEmail(), createdUser.getEmail());
        verify(userRepositoryMock).save(any(User.class));  // Ensure save was called
    }

    @Test
    public void testFindAllUsers() {
        // Mock behavior: return a list with one user
        when(userRepositoryMock.findAll()).thenReturn(List.of(user));

        // Call the service method to find all users
        List<UserDTO> users = userService.findAllUsers();

        // Assertions to verify the result
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(user.getName(), users.get(0).getName());
    }

    @Test
    public void testFindUserById() {
        // Mock behavior: return the mock user object
        when(userRepositoryMock.findById(anyLong())).thenReturn(Optional.of(user));

        // Call the service method to find the user by ID
        Optional<UserDTO> foundUser = userService.findUserById(user.getId());

        // Assertions to verify the result
        assertTrue(foundUser.isPresent());
        assertEquals(user.getName(), foundUser.get().getName());
        assertEquals(user.getEmail(), foundUser.get().getEmail());
    }

    @Test
    public void testUpdateUser() {
        // Creating a UserDTO object with updated data
        UserDTO userDTO = new UserDTO(1L, "Updated Name", "updated.email@example.com");

        // Mock behavior: return the mock user object when finding by ID and saving
        when(userRepositoryMock.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepositoryMock.save(any(User.class))).thenReturn(user);

        // Call the service method to update the user
        Optional<UserDTO> updatedUser = userService.updateUser(user.getId(), userDTO);

        // Assertions to verify the result
        assertTrue(updatedUser.isPresent());
        assertEquals(userDTO.getName(), updatedUser.get().getName());
        assertEquals(userDTO.getEmail(), updatedUser.get().getEmail());
    }

    @Test
    public void testDeleteUser() {
        // Mock behavior: return true when checking if the user exists by ID
        when(userRepositoryMock.existsById(anyLong())).thenReturn(true);

        // Call the service method to delete the user
        boolean result = userService.deleteUser(user.getId());

        // Assertions to verify the result
        assertTrue(result);
        verify(userRepositoryMock).deleteById(anyLong());  // Ensure delete was called
    }

    @Test
    public void testValidateAndSave() {
        // Creating a UserRegistrationDTO object to simulate user registration
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO("jane_doe", "password123", "Jane Doe", "jane.doe@example.com", "1992-06-15");

        // Mock behavior: when checking if email already exists, return false
        when(userRepositoryMock.existsByEmail(registrationDTO.getEmail())).thenReturn(false);

        // Mock behavior: save the user and return the mock user
        when(userRepositoryMock.save(any(User.class))).thenReturn(user);

        // Mock JWT token generation
        when(jwtUtilMock.generateToken(anyString())).thenReturn("mockToken");

        // Call the service method to validate and save the user
        AuthenticationResponse response = userService.validateAndSave(registrationDTO);

        // Assertions to verify the result
        assertNotNull(response);
        assertEquals("mockToken", response.getJwt());
    }

    @Test
    public void testValidateAndLogin() {
        // Creating an AuthenticationRequest object for login
        AuthenticationRequest authRequest = new AuthenticationRequest("john_doe", "password123");

        // Mock behavior: authenticate successfully
        when(authenticationManagerMock.authenticate(any())).thenReturn(mock(Authentication.class));

        // Mock behavior: return a userDetails mock when loading user by username
        when(userDetailsServiceMock.loadUserByUsername(anyString())).thenReturn(mock(UserDetails.class));

        // Mock JWT token generation
        when(jwtUtilMock.generateToken(anyString())).thenReturn("mockToken");

        // Call the service method to validate and login
        AuthenticationResponse response = userService.validateAndLogin(authRequest);

        // Assertions to verify the result
        assertNotNull(response);
        assertEquals("mockToken", response.getJwt());
    }
}
