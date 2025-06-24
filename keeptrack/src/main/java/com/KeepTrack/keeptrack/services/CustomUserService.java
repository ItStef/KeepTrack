package com.KeepTrack.keeptrack.services;

import com.KeepTrack.keeptrack.models.User;
import com.KeepTrack.keeptrack.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.Collections;



@Service
public class CustomUserService implements UserDetailsService {

    private UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().getName().toUpperCase());

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(), user.getPassword(), Collections.singleton(authority)
        );
    }

}
