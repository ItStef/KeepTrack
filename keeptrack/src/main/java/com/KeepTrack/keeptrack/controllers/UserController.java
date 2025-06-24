package com.KeepTrack.keeptrack.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.KeepTrack.keeptrack.services.UserService;
import com.KeepTrack.keeptrack.models.User;
import com.KeepTrack.keeptrack.dto.*;
import java.util.Optional;
import java.util.HashMap;
import java.util.List;
import com.KeepTrack.keeptrack.models.Role;
import com.KeepTrack.keeptrack.repositories.RoleRepository;
import com.KeepTrack.keeptrack.repositories.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.KeepTrack.keeptrack.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Map;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8081")
public class UserController {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegisterDTO userDTO) {
        if (userService.findUserByUsername(userDTO.getUsername()).isPresent()) {
            return ResponseEntity.badRequest()
                .body("Username already taken..");
        }

        User user = new User();
        Role role = roleRepository.findByName(userDTO.getRole()).orElseThrow(() -> new IllegalArgumentException("Invalid role: " + userDTO.getRole()));
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword()); 
        user.setEmail(userDTO.getEmail());
        user.setRole(role);

        userService.saveUser(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserLoginDTO loginDTO) {

        Map<String, Object> response = new HashMap<>();
        var userOptional = userService.findUserByUsername(loginDTO.getUsername());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
                String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
                
                response.put("success", true);
                response.put("message", "User logged in as " + user.getRole().getName());
                response.put("username", user.getUsername());
                response.put("token", token);
                response.put("role", user.getRole().getName());
                return ResponseEntity.ok(response);
            }
        }
        
        response.put("success", false);
        response.put("error", "Wrong username or password..");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @GetMapping
    public ResponseEntity<String> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users found..");
        }
        return ResponseEntity.ok("Found " + users.size() + " users");
    }


    @GetMapping("/{id}")
    public ResponseEntity<String> getUserById(@PathVariable Long id) {
        Optional<User> userOptional = userService.getUser(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Korisnik nije pronađen!");
        }
        
        User user = userOptional.get();
        return ResponseEntity.ok("User found: " + user.getUsername());
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserRegisterDTO userDTO) {
        Optional<User> userOptional = userService.getUser(id);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found..");
        }
        
        User user = userOptional.get();
        Role role = roleRepository.findByName(userDTO.getRole()).orElseThrow(() -> new IllegalArgumentException("Nevalidna uloga: " + userDTO.getRole()));
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setRole(role);

        userService.saveUser(user);
        return ResponseEntity.ok("User successfully updated..");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        Optional<User> userOptional = userService.getUser(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found..");
        }
        userService.deleteUser(id);
        return ResponseEntity.ok("User successfully deleted..");
    }

    @GetMapping("/role")
    public String getRole(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getRole().getName();
    }

}
