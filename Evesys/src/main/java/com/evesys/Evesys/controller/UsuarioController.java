package com.evesys.Evesys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.evesys.Evesys.model.Usuario;
import com.evesys.Evesys.repository.UsuarioRepository;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    
    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }

    // Muestra la página de login
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // Muestra la página de inicio después de un login exitoso
    @GetMapping("/home")
    public String showHomePage(Model model, Authentication authentication) {
        
        String rol = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("rol", rol);
        return "home";
    }

    // Muestra el formulario de registro público
@GetMapping("/registro")
public String mostrarFormularioRegistro(Model model) {
    model.addAttribute("usuario", new Usuario());
    return "registro"; 
}

// Procesa y guarda el nuevo usuario del formulario público
@PostMapping("/registro")
public String guardarRegistro(@ModelAttribute Usuario usuario) {
    
    usuario.setRol("USER");
    
    usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

    usuarioRepository.save(usuario);

    // Redirigir a la página de login con un mensaje de éxito
    return "redirect:/login?registro_exitoso=true";
}

    // ADMIN 

    // Muestra la lista de todos los usuarios
    @GetMapping("/admin/usuarios")
    public String listUsers(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "admin/usuarios";
    }

    // Muestra el formulario para crear un nuevo usuario
    @GetMapping("/admin/usuarios/nuevo")
    public String showCreateUserForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "admin/form";
    }

    // Muestra el formulario para editar un usuario existente
    @GetMapping("/admin/usuarios/editar/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de Usuario inválido:" + id));
        model.addAttribute("usuario", usuario);
        return "admin/form";
    }

    // Guarda un usuario (nuevo o editado)
    @PostMapping("/admin/usuarios/guardar")
    public String saveUser(@ModelAttribute Usuario usuario) {
       
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);
        return "redirect:/admin/usuarios";
    }
    
    // Elimina un usuario
    @GetMapping("/admin/usuarios/eliminar/{id}")
    public String deleteUser(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
        return "redirect:/admin/usuarios";
    }
    
    //  USUARIO 
    
    // Muestra el perfil del usuario logueado para que pueda editarlo
    @GetMapping("/perfil")
    public String showProfileForm(Model model, Authentication authentication) {
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUserName(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        model.addAttribute("usuario", usuario);
        return "perfil/form";
    }

    // Guarda los cambios del perfil del usuario
    @PostMapping("/perfil/guardar")
    public String saveProfile(@ModelAttribute Usuario usuario, Authentication authentication) {
        String username = authentication.getName();
        Usuario usuarioActual = usuarioRepository.findByUserName(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Actualiza solo los campos que el usuario puede cambiar
        usuarioActual.setNombreCompleto(usuario.getNombreCompleto());
        usuarioActual.setCorreo(usuario.getCorreo());
        usuarioActual.setDireccion(usuario.getDireccion());
        usuarioActual.setTelefono(usuario.getTelefono());
        
        if (!usuario.getPassword().isEmpty()) {
            usuarioActual.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        
        usuarioRepository.save(usuarioActual);
        return "redirect:/home?success";
    }
}