package com.evesys.Evesys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.evesys.Evesys.model.Evento;
import com.evesys.Evesys.model.Usuario;
import com.evesys.Evesys.repository.EquipoRepository;
import com.evesys.Evesys.repository.EventoRepository;
import com.evesys.Evesys.repository.ServicioRepository;
import com.evesys.Evesys.repository.UsuarioRepository;
import java.util.List;

@Controller
public class EventoController {

    @Autowired
    private EventoRepository eventoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ServicioRepository servicioRepository;
    @Autowired
    private EquipoRepository equipoRepository;

    
    @GetMapping("/eventos/nuevo")
    public String mostrarFormularioDeEvento(Model model) {
        
        model.addAttribute("evento", new Evento());
        
        model.addAttribute("serviciosDisponibles", servicioRepository.findAll());
        model.addAttribute("equiposDisponibles", equipoRepository.findAll());
        return "eventos/form";
    }

    // Guarda el nuevo evento en la base de datos
    @PostMapping("/eventos/guardar")
    public String guardarEvento(@ModelAttribute Evento evento, Authentication authentication) {
        
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUserName(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        
        evento.setUsuario(usuario);

        
        evento.setEstado("PENDIENTE");

         
    double costoTotalCalculado = 0.0;
    
    if (evento.getServicios() != null) {
        costoTotalCalculado += evento.getServicios().stream().mapToDouble(s -> s.getCosto()).sum();
    }
    
    if (evento.getEquipos() != null) {
        costoTotalCalculado += evento.getEquipos().stream().mapToDouble(e -> e.getCostoAlquiler()).sum();
    }
    evento.setCostoTotal(costoTotalCalculado);
    

    
        eventoRepository.save(evento);

    
        return "redirect:/home?evento_creado=true";
    }

    
    @GetMapping("/mis-eventos")
    public String mostrarMisEventos(Model model, Authentication authentication) {
    
         String username = authentication.getName();
         Usuario usuario = usuarioRepository.findByUserName(username)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

    
         List<Evento> misEventos = eventoRepository.findByUsuario(usuario);

    
         model.addAttribute("eventos", misEventos);

        return "eventos/lista";
    }
}