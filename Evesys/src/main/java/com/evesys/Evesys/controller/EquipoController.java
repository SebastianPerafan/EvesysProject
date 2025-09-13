package com.evesys.Evesys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.evesys.Evesys.model.Equipo;
import com.evesys.Evesys.repository.EquipoRepository;

@Controller
@RequestMapping("/admin/equipos")
public class EquipoController {

    @Autowired
    private EquipoRepository equipoRepository;

    @GetMapping
    public String listarEquipos(Model model) {
        model.addAttribute("equipos", equipoRepository.findAll());
        return "admin/equipos/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("equipo", new Equipo());
        return "admin/equipos/form";
    }

    @PostMapping("/guardar")
    public String guardarEquipo(@ModelAttribute Equipo equipo) {
        equipoRepository.save(equipo);
        return "redirect:/admin/equipos";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Equipo equipo = equipoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de Equipo inválido:" + id));
        model.addAttribute("equipo", equipo);
        return "admin/equipos/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarEquipo(@PathVariable Long id) {
        equipoRepository.deleteById(id);
        return "redirect:/admin/equipos";
    }
}