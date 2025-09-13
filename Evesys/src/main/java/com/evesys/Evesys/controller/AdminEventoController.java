package com.evesys.Evesys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.evesys.Evesys.model.Evento;
import com.evesys.Evesys.repository.EventoRepository;

@Controller
@RequestMapping("/admin/eventos")
public class AdminEventoController {

    @Autowired
    private EventoRepository eventoRepository;

    @GetMapping
    public String listarTodosLosEventos(Model model) {
        model.addAttribute("eventos", eventoRepository.findAll());
        return "admin/eventos/lista";
    }

    @GetMapping("/{id}")
    public String verDetalleDeEvento(@PathVariable Long id, Model model) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de Evento inválido:" + id));
        model.addAttribute("evento", evento);
        return "admin/eventos/detalle";
    }

    @PostMapping("/actualizar-estado")
    public String actualizarEstadoEvento(@RequestParam Long eventoId, @RequestParam String estado) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new IllegalArgumentException("ID de Evento inválido:" + eventoId));
        evento.setEstado(estado);
        eventoRepository.save(evento);
        return "redirect:/admin/eventos/" + eventoId;
    }

    
    @GetMapping("/eliminar/{id}")
    public String eliminarEvento(@PathVariable Long id) {
        eventoRepository.deleteById(id);
        
        return "redirect:/admin/eventos";
    }
}