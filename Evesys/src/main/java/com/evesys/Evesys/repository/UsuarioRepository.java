package com.evesys.Evesys.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.evesys.Evesys.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    
    Optional<Usuario> findByUserName(String userName);
}