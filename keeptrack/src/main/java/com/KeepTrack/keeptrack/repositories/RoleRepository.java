package com.KeepTrack.keeptrack.repositories;

import com.KeepTrack.keeptrack.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;




public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
