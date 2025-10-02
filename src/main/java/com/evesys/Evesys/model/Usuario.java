package com.evesys.Evesys.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "usuarios")
@Data 
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20)
    private String rol;

   
    @Column(nullable = false, length = 100)
    private String nombreCompleto;

    @Column(unique = true, nullable = false, length = 100)
    private String correo;

    @Column(length = 100)
    private String direccion;

    @Column(length = 20)
    private String telefono;
}