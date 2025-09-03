package com.gestioncrm.controller;

import com.gestioncrm.repository.PersonaRepository;
import com.gestioncrm.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador de prueba para verificar la funcionalidad básica
 */
@RestController
@RequestMapping("/test")
@CrossOrigin
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);
    
    private final PersonaRepository personaRepository;
    private final UsuarioRepository usuarioRepository;

    public TestController(PersonaRepository personaRepository, UsuarioRepository usuarioRepository) {
        this.personaRepository = personaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Endpoint de prueba para verificar la salud de la aplicación
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        log.info("Verificando salud de la aplicación");
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint de prueba para verificar la base de datos
     */
    @GetMapping("/database")
    public ResponseEntity<Map<String, Object>> testDatabase() {
        log.info("Probando conexión a la base de datos");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Contar registros en las tablas
            long countPersonas = personaRepository.count();
            long countUsuarios = usuarioRepository.count();
            
            response.put("success", true);
            response.put("personas", countPersonas);
            response.put("usuarios", countUsuarios);
            response.put("mensaje", "Base de datos funcionando correctamente");
            
            log.info("Base de datos OK - Personas: {}, Usuarios: {}", countPersonas, countUsuarios);
            
        } catch (Exception e) {
            log.error("Error al probar la base de datos", e);
            
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("mensaje", "Error en la base de datos");
            
            return ResponseEntity.status(500).body(response);
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint de prueba para crear una persona de prueba
     */
    @PostMapping("/persona-test")
    public ResponseEntity<Map<String, Object>> crearPersonaTest() {
        log.info("Creando persona de prueba");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Crear una persona de prueba
            com.gestioncrm.model.Persona persona = new com.gestioncrm.model.Persona();
            persona.setNombre("Test");
            persona.setApellido("Usuario");
            persona.setCedula("TEST" + System.currentTimeMillis());
            persona.setEmail("test@test.com");
            persona.setEdad(25);
            persona.setSexo(com.gestioncrm.model.Persona.Sexo.M);
            persona.setRol("Test");
            
            com.gestioncrm.model.Persona personaGuardada = personaRepository.save(persona);
            
            response.put("success", true);
            response.put("mensaje", "Persona de prueba creada exitosamente");
            response.put("persona", personaGuardada);
            
            log.info("Persona de prueba creada con ID: {}", personaGuardada.getId());
            
        } catch (Exception e) {
            log.error("Error al crear persona de prueba", e);
            
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("mensaje", "Error al crear persona de prueba");
            
            return ResponseEntity.status(500).body(response);
        }
        
        return ResponseEntity.ok(response);
    }
}
