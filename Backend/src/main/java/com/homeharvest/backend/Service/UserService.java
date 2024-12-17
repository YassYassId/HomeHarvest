package com.homeharvest.backend.Service;

import com.homeharvest.backend.DTO.JwtAuthenticationResponse;
import com.homeharvest.backend.DTO.RefreshTokenRequest;
import com.homeharvest.backend.DTO.RegisterRequest;
import com.homeharvest.backend.DTO.SignInRequest;
import com.homeharvest.backend.Entity.Role;
import com.homeharvest.backend.Entity.User;
import com.homeharvest.backend.Repository.UserRepository;
import lombok.Data;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Data
@Service
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, @Lazy AuthenticationManager authenticationManager, JWTService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }


    public UserDetailsService userDetailsService(){
        return userRepository::findByEmail;
    }


    public JwtAuthenticationResponse signin(SignInRequest signinRequest) {
        System.out.println("Received Email: " + signinRequest.getEmail());
        System.out.println("Received Password: " + signinRequest.getPassword());

        // Fetch the user from the database
        var user = userRepository.findByEmail(signinRequest.getEmail());
        if (user == null) {
            System.out.println("No user found for email: " + signinRequest.getEmail());
            throw new RuntimeException("Invalid email or password");
        }

        System.out.println("User found: " + user.getEmail() + ", Password: " + user.getPassword());

        // Authenticate the user
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword())
        );

        System.out.println("Authentication Successful: " + authenticate);

        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);

        return jwtAuthenticationResponse;
    }

    public void saveUser(User user) {
        var existUser = userRepository.findByEmail(user.getUsername());
        if (existUser != null){
            userRepository.save(user);
        }

    }

    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest){
        String email = jwtService.extractUsername(refreshTokenRequest.getToken());
        User user = userRepository.findByEmail(email);
        if(jwtService.isTokenValid(refreshTokenRequest.getToken(), user) && user!=null){
            var jwt = jwtService.generateToken(user);

            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());

            return jwtAuthenticationResponse;
        }
        return null;
    }

    public JwtAuthenticationResponse register(RegisterRequest registerRequest) {
        User user = new User();

        user.setPrenom(registerRequest.getPrenom());
        user.setNom(registerRequest.getNom());
        user.setTel(registerRequest.getTel());
        user.setAdresse(registerRequest.getAdresse());
        user.setEmail(registerRequest.getEmail());
        user.setRole(Role.USER);

        System.out.println("Raw Password: " + registerRequest.getPassword());
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        System.out.println("Encoded Password: " + encodedPassword);

        user.setPassword(encodedPassword);

        userRepository.save(user);

        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        return jwtAuthenticationResponse;
    }
}
