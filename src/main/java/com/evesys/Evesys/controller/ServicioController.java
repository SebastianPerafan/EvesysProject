package com.evesys.Evesys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.evesys.Evesys.model.Servicio;
import com.evesys.Evesys.repository.ServicioRepository;

@Controller
@RequestMapping("/admin/servicios") 
public class ServicioController {

    @Autowired
    private ServicioRepository servicioRepository;

    // Muestra la lista de todos los servicios
    @GetMapping
    public String listarServicios(Model model) {
        model.addAttribute("servicios", servicioRepository.findAll());
        return "admin/servicios/lista"; 
    }

    // Muestra el formulario para crear un nuevo servicio
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("servicio", new Servicio());
        return "admin/servicios/form";
    }

    // Guarda un servicio 
    @PostMapping("/guardar")
    public String guardarServicio(@ModelAttribute Servicio servicio) {
        servicioRepository.save(servicio);
        return "redirect:/admin/servicios";
    }

    // Muestra el formulario para editar un servicio
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de Servicio inválido:" + id));
        model.addAttribute("servicio", servicio);
        return "admin/servicios/form";
    }

    // Elimina un servicio
    @GetMapping("/eliminar/{id}")
    public String eliminarServicio(@PathVariable Long id) {
        servicioRepository.deleteById(id);
        return "redirect:/admin/servicios";
    }
}