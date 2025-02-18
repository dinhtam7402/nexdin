package com.nexdin.clothingstore.repository;

import com.nexdin.clothingstore.domain.Roles;
import com.nexdin.clothingstore.domain.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository extends JpaRepository<Roles, String> {
    Roles findByName(ERole name);
}
