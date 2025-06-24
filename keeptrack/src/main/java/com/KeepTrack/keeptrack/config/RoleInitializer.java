package com.KeepTrack.keeptrack.config;

import com.KeepTrack.keeptrack.models.Role;
import com.KeepTrack.keeptrack.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleInitializer {

    @Bean
    public CommandLineRunner setupRoles(RoleRepository roleRepository) {
        return args -> {
            String[] roles = {"BOSS", "MANAGER", "USER"};
            if (roleRepository.count() > 0) {
                return; 
            }else {
                for (String roleName : roles) {
                roleRepository.findByName(roleName)
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName(roleName);
                        return roleRepository.save(role);
                });
            }
            }
        };
    }
}