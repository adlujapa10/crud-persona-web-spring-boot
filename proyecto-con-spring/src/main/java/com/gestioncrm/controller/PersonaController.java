package com.gestioncrm.controller;

import com.gestioncrm.model.Persona;
import com.gestioncrm.service.PersonaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Controlador para la gestión de personas
 * Maneja las peticiones HTTP y la lógica de negocio relacionada con personas
 */
@Controller
@RequestMapping("/personas")
@CrossOrigin
public class PersonaController {

    private static final Logger log = LoggerFactory.getLogger(PersonaController.class);
    
    private final PersonaService personaService;

    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    // ========== ENDPOINTS THYMELEAF (VISTAS) ==========

    /**
     * Página principal - Lista de personas
     */
    @GetMapping
    public String listarPersonas(Model model) {
        log.info("Mostrando lista de personas");
        
        try {
            List<Persona> personas = personaService.obtenerTodas();
            PersonaService.EstadisticasPersonas estadisticas = personaService.obtenerEstadisticas();
            
            log.info("Total de personas obtenidas: {}", personas.size());
            for (Persona persona : personas) {
                log.debug("Persona: ID={}, Nombre={}, Cédula={}", persona.getId(), persona.getNombreCompleto(), persona.getCedula());
            }
            
            model.addAttribute("personas", personas);
            model.addAttribute("estadisticas", estadisticas);
            
            return "personas/lista";
        } catch (Exception e) {
            log.error("Error al obtener personas", e);
            model.addAttribute("error", "Error al cargar la lista de personas: " + e.getMessage());
            return "personas/lista";
        }
    }

    /**
     * Formulario para crear nueva persona
     */
    @GetMapping("/nueva")
    public String mostrarFormularioNueva(Model model) {
        log.info("Mostrando formulario para nueva persona");
        
        model.addAttribute("persona", new Persona());
        model.addAttribute("esEdicion", false);
        model.addAttribute("sexos", Persona.Sexo.values());
        
        return "personas/formulario";
    }

    /**
     * Formulario para editar persona existente
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        log.info("Mostrando formulario para editar persona con ID: {}", id);
        
        try {
            Persona persona = personaService.obtenerPorId(id);
            model.addAttribute("persona", persona);
            model.addAttribute("esEdicion", true);
            model.addAttribute("sexos", Persona.Sexo.values());
            
            return "personas/formulario";
        } catch (EntityNotFoundException e) {
            log.warn("Persona no encontrada con ID: {}", id);
            return "redirect:/personas?error=Persona no encontrada";
        }
    }

    /**
     * Guardar nueva persona
     */
    @PostMapping
    public String guardarPersona(@Valid @ModelAttribute Persona persona, 
                                BindingResult bindingResult, 
                                Model model, 
                                RedirectAttributes redirectAttributes) {
        log.info("Guardando nueva persona: {}", persona.getNombreCompleto());
        
        if (bindingResult.hasErrors()) {
            log.warn("Errores de validación en persona: {}", bindingResult.getAllErrors());
            model.addAttribute("esEdicion", false);
            model.addAttribute("sexos", Persona.Sexo.values());
            return "personas/formulario";
        }
        
        try {
            personaService.guardar(persona);
            redirectAttributes.addFlashAttribute("mensaje", "Persona guardada exitosamente");
            log.info("Persona guardada exitosamente con ID: {}", persona.getId());
            return "redirect:/personas";
        } catch (IllegalArgumentException e) {
            log.warn("Error al guardar persona: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("esEdicion", false);
            model.addAttribute("sexos", Persona.Sexo.values());
            return "personas/formulario";
        }
    }

    /**
     * Actualizar persona existente
     */
    @PostMapping("/actualizar/{id}")
    public String actualizarPersona(@PathVariable Long id, 
                                   @Valid @ModelAttribute Persona persona, 
                                   BindingResult bindingResult, 
                                   Model model, 
                                   RedirectAttributes redirectAttributes) {
        log.info("Actualizando persona con ID: {}", id);
        
        if (bindingResult.hasErrors()) {
            log.warn("Errores de validación en persona: {}", bindingResult.getAllErrors());
            model.addAttribute("esEdicion", true);
            model.addAttribute("sexos", Persona.Sexo.values());
            return "personas/formulario";
        }
        
        try {
            personaService.actualizar(id, persona);
            redirectAttributes.addFlashAttribute("mensaje", "Persona actualizada exitosamente");
            log.info("Persona actualizada exitosamente con ID: {}", id);
            return "redirect:/personas";
        } catch (EntityNotFoundException e) {
            log.warn("Persona no encontrada con ID: {}", id);
            return "redirect:/personas?error=Persona no encontrada";
        } catch (IllegalArgumentException e) {
            log.warn("Error al actualizar persona: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("esEdicion", true);
            model.addAttribute("sexos", Persona.Sexo.values());
            return "personas/formulario";
        }
    }

    /**
     * Eliminar persona
     */
    @PostMapping("/eliminar/{id}")
    public String eliminarPersona(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("Eliminando persona con ID: {}", id);
        
        try {
            personaService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Persona eliminada exitosamente");
            log.info("Persona eliminada exitosamente con ID: {}", id);
        } catch (EntityNotFoundException e) {
            log.warn("Persona no encontrada con ID: {}", id);
            redirectAttributes.addFlashAttribute("error", "Persona no encontrada");
        } catch (Exception e) {
            log.error("Error al eliminar persona con ID: {}", id, e);
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la persona");
        }
        
        return "redirect:/personas";
    }

    /**
     * Buscar personas por término
     */
    @GetMapping("/buscar")
    public String buscarPersonas(@RequestParam String termino, Model model) {
        log.info("Buscando personas con término: {}", termino);
        
        try {
            List<Persona> personas = personaService.buscarPorTermino(termino);
            model.addAttribute("personas", personas);
            model.addAttribute("terminoBusqueda", termino);
            model.addAttribute("estadisticas", personaService.obtenerEstadisticas());
            
            return "personas/lista";
        } catch (Exception e) {
            log.error("Error al buscar personas", e);
            model.addAttribute("error", "Error al buscar personas: " + e.getMessage());
            return "personas/lista";
        }
    }

    /**
     * Filtrar personas por rol
     */
    @GetMapping("/filtrar/rol/{rol}")
    public String filtrarPorRol(@PathVariable String rol, Model model) {
        log.info("Filtrando personas por rol: {}", rol);
        
        try {
            List<Persona> personas = personaService.buscarPorRol(rol);
            model.addAttribute("personas", personas);
            model.addAttribute("filtroRol", rol);
            model.addAttribute("estadisticas", personaService.obtenerEstadisticas());
            
            return "personas/lista";
        } catch (Exception e) {
            log.error("Error al filtrar personas por rol", e);
            model.addAttribute("error", "Error al filtrar personas: " + e.getMessage());
            return "personas/lista";
        }
    }

    /**
     * Filtrar personas por sexo
     */
    @GetMapping("/filtrar/sexo/{sexo}")
    public String filtrarPorSexo(@PathVariable Persona.Sexo sexo, Model model) {
        log.info("Filtrando personas por sexo: {}", sexo);
        
        try {
            List<Persona> personas = personaService.buscarPorSexo(sexo);
            model.addAttribute("personas", personas);
            model.addAttribute("filtroSexo", sexo);
            model.addAttribute("estadisticas", personaService.obtenerEstadisticas());
            
            return "personas/lista";
        } catch (Exception e) {
            log.error("Error al filtrar personas por sexo", e);
            model.addAttribute("error", "Error al filtrar personas: " + e.getMessage());
            return "personas/lista";
        }
    }

    // ========== ENDPOINTS REST PARA FRONTEND REACT ==========

    /**
     * Obtener todas las personas (JSON)
     */
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<Persona>> obtenerTodasPersonas() {
        log.info("API: Obteniendo todas las personas");
        try {
            List<Persona> personas = personaService.obtenerTodas();
            return ResponseEntity.ok(personas);
        } catch (Exception e) {
            log.error("Error al obtener personas", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtener persona por ID (JSON)
     */
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Persona> obtenerPersonaPorId(@PathVariable Long id) {
        log.info("API: Obteniendo persona con ID: {}", id);
        try {
            Persona persona = personaService.obtenerPorId(id);
            return ResponseEntity.ok(persona);
        } catch (EntityNotFoundException e) {
            log.warn("Persona no encontrada con ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al obtener persona", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Crear nueva persona (JSON)
     */
    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<Persona> crearPersona(@Valid @RequestBody Persona persona) {
        log.info("API: Creando nueva persona: {}", persona.getNombreCompleto());
        try {
            Persona personaGuardada = personaService.guardar(persona);
            return ResponseEntity.status(HttpStatus.CREATED).body(personaGuardada);
        } catch (IllegalArgumentException e) {
            log.warn("Error al crear persona: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error al crear persona", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Actualizar persona (JSON)
     */
    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Persona> actualizarPersona(@PathVariable Long id, @Valid @RequestBody Persona persona) {
        log.info("API: Actualizando persona con ID: {}", id);
        try {
            Persona personaActualizada = personaService.actualizar(id, persona);
            return ResponseEntity.ok(personaActualizada);
        } catch (EntityNotFoundException e) {
            log.warn("Persona no encontrada con ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al actualizar persona", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Eliminar persona (JSON)
     */
    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> eliminarPersona(@PathVariable Long id) {
        log.info("API: Eliminando persona con ID: {}", id);
        try {
            personaService.eliminar(id);
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Persona eliminada exitosamente");
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            log.warn("Persona no encontrada con ID: {}", id);
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Persona no encontrada");
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            log.error("Error al eliminar persona con ID: {}", id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error al eliminar persona: " + e.getMessage());
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Buscar personas por término (JSON)
     */
    @GetMapping("/api/buscar")
    @ResponseBody
    public ResponseEntity<List<Persona>> buscarPersonasAPI(@RequestParam String termino) {
        log.info("API: Buscando personas con término: {}", termino);
        try {
            List<Persona> personas = personaService.buscarPorTermino(termino);
            return ResponseEntity.ok(personas);
        } catch (Exception e) {
            log.error("Error al buscar personas", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Filtrar personas por rol (JSON)
     */
    @GetMapping("/api/filtrar/rol/{rol}")
    @ResponseBody
    public ResponseEntity<List<Persona>> filtrarPorRolAPI(@PathVariable String rol) {
        log.info("API: Filtrando personas por rol: {}", rol);
        try {
            List<Persona> personas = personaService.buscarPorRol(rol);
            return ResponseEntity.ok(personas);
        } catch (Exception e) {
            log.error("Error al filtrar personas por rol", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Filtrar personas por sexo (JSON)
     */
    @GetMapping("/api/filtrar/sexo/{sexo}")
    @ResponseBody
    public ResponseEntity<List<Persona>> filtrarPorSexoAPI(@PathVariable Persona.Sexo sexo) {
        log.info("API: Filtrando personas por sexo: {}", sexo);
        try {
            List<Persona> personas = personaService.buscarPorSexo(sexo);
            return ResponseEntity.ok(personas);
        } catch (Exception e) {
            log.error("Error al filtrar personas por sexo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtener estadísticas (JSON)
     */
    @GetMapping("/api/estadisticas")
    @ResponseBody
    public ResponseEntity<PersonaService.EstadisticasPersonas> obtenerEstadisticasAPI() {
        log.info("API: Obteniendo estadísticas");
        try {
            PersonaService.EstadisticasPersonas estadisticas = personaService.obtenerEstadisticas();
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            log.error("Error al obtener estadísticas", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Verificar si existe cédula (JSON)
     */
    @GetMapping("/api/verificar-cedula/{cedula}")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> verificarCedula(@PathVariable String cedula) {
        log.info("API: Verificando cédula: {}", cedula);
        try {
            boolean existe = personaService.existePorCedula(cedula);
            Map<String, Boolean> response = new HashMap<>();
            response.put("existe", existe);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al verificar cédula", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Limpiar datos duplicados (JSON)
     */
    @PostMapping("/api/limpiar-duplicados")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> limpiarDuplicadosAPI() {
        log.info("API: Solicitud para limpiar duplicados");
        
        try {
            personaService.limpiarDuplicados();
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Datos duplicados eliminados correctamente");
            response.put("success", true);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al limpiar duplicados", e);
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error al limpiar duplicados: " + e.getMessage());
            response.put("success", false);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
