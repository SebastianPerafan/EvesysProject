package com.evesys.Evesys.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.evesys.Evesys.model.Usuario;
import com.evesys.Evesys.repository.UsuarioRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(UsuarioRepository usuarioRepository) {
        return args -> {
            // Busca el usuario 'admin'. Si no existe, crea una nueva instancia.
            Usuario admin = usuarioRepository.findByUserName("admin")
                    .orElse(new Usuario());

            // Se asegura de que todos los datos del admin estén correctos
            admin.setUserName("admin");
            admin.setPassword(new BCryptPasswordEncoder().encode("123")); 
            admin.setRol("ADMIN");
            admin.setNombreCompleto("Administrador del Sistema");
            admin.setCorreo("admin@evesys.com");
            admin.setDireccion("Oficina Principal");
            admin.setTelefono("N/A");

            // Guarda los cambios (crea o actualiza al usuario admin)
            usuarioRepository.save(admin);

            System.out.println("✅ Usuario 'admin' verificado y actualizado con éxito!");
        };
    }
}