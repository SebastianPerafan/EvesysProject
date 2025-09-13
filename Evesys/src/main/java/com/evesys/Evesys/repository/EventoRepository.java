package com.evesys.Evesys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.evesys.Evesys.model.Evento;
import com.evesys.Evesys.model.Usuario;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findByUsuario(Usuario usuario);
}