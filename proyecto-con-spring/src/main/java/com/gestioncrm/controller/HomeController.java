package com.gestioncrm.controller;

import com.gestioncrm.service.PersonaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para la página principal y navegación general
 */
@Controller
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);
    
    private final PersonaService personaService;

    public HomeController(PersonaService personaService) {
        this.personaService = personaService;
    }

    /**
     * Página principal del sistema
     */
    @GetMapping("/")
    public String home(Model model) {
        log.info("Mostrando página principal");
        
        try {
            // Obtener estadísticas para mostrar en el dashboard
            PersonaService.EstadisticasPersonas estadisticas = personaService.obtenerEstadisticas();
            model.addAttribute("estadisticas", estadisticas);
            
            return "home";
        } catch (Exception e) {
            log.error("Error al cargar la página principal", e);
            model.addAttribute("error", "Error al cargar la información del sistema");
            return "home";
        }
    }

    /**
     * Página de información del sistema
     */
    @GetMapping("/acerca")
    public String acerca() {
        log.info("Mostrando página de información del sistema");
        return "acerca";
    }

    /**
     * Página de contacto
     */
    @GetMapping("/contacto")
    public String contacto() {
        log.info("Mostrando página de contacto");
        return "contacto";
    }
}
