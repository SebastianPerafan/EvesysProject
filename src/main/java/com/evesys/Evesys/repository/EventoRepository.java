package com.evesys.Evesys.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.evesys.Evesys.model.Evento;
import com.evesys.Evesys.model.Usuario;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    List<Evento> findByUsuario(Usuario usuario);

    // Nueva consulta para filtrar por rango de fechas y/o estado
    @Query("SELECT e FROM Evento e WHERE " +
           "(:fechaInicio IS NULL OR e.fecha >= :fechaInicio) AND " +
           "(:fechaFin IS NULL OR e.fecha <= :fechaFin) AND " +
           "(:estado IS NULL OR :estado = '' OR e.estado = :estado)")
    List<Evento> findByFiltros(
        @Param("fechaInicio") LocalDate fechaInicio,
        @Param("fechaFin") LocalDate fechaFin,
        @Param("estado") String estado
    );
}