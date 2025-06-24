package com.KeepTrack.keeptrack.services;
import org.springframework.stereotype.Service;
import com.KeepTrack.keeptrack.models.User;
import com.KeepTrack.keeptrack.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String createUser(String username, String password) {

        if (userRepository.findByUsername(username).isPresent()) {
            return "Username already exists";
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); 
        userRepository.save(user);
        return "User created successfully";
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUser(Long id) { 
        return userRepository.findById(id); 
    }
    
    public List<User> getAllUsers() { 
        return userRepository.findAll(); 
    }

    public User saveUser(User user) { 
        return userRepository.save(user); 
    }

    public void deleteUser(Long id) { 
        userRepository.deleteById(id); 
    }
}

