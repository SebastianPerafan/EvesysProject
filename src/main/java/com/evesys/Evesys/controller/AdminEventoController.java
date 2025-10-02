package com.evesys.Evesys.controller;

// NUEVAS IMPORTACIONES PARA LA FUNCIONALIDAD DE REPORTES
import com.evesys.Evesys.utils.PdfGenerator;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Importaciones existentes
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

    // NUEVA INYECCIÓN PARA EL GENERADOR DE PDF
    @Autowired
    private PdfGenerator pdfGenerator;

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
    
    // --- NUEVOS MÉTODOS PARA REPORTES ---

    // Muestra la página de reportes con filtros
    @GetMapping("/reporte")
    public String mostrarVistaDeReporte(
            @RequestParam(required = false) LocalDate fechaInicio,
            @RequestParam(required = false) LocalDate fechaFin,
            @RequestParam(required = false) String estado,
            Model model) {
        
        List<Evento> eventosFiltrados = eventoRepository.findByFiltros(fechaInicio, fechaFin, estado);
        model.addAttribute("eventos", eventosFiltrados);
        return "admin/eventos/vista-reporte";
    }

    // Genera y descarga el reporte en PDF
    @GetMapping("/reporte/pdf")
    public void generarReportePdf(
            @RequestParam(required = false) LocalDate fechaInicio,
            @RequestParam(required = false) LocalDate fechaFin,
            @RequestParam(required = false) String estado,
            HttpServletResponse response) {
        
        List<Evento> eventosFiltrados = eventoRepository.findByFiltros(fechaInicio, fechaFin, estado);
        Map<String, Object> variables = new HashMap<>();
        variables.put("eventos", eventosFiltrados);

        try {
            pdfGenerator.generarPdfDeHtml("admin/eventos/reporte-template", variables, response);
        } catch (Exception e) {
            // Aquí podrías manejar el error de una forma más elegante
            e.printStackTrace();
        }
    }
}