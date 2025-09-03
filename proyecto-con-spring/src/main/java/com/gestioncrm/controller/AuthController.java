package com.gestioncrm.controller;

import com.gestioncrm.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para la autenticación y autorización
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    
    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Verificar si existe un usuario con el nombre de usuario dado
     */
    @GetMapping("/verificar-usuario/{usuario}")
    public ResponseEntity<Map<String, Boolean>> verificarUsuario(@PathVariable String usuario) {
        log.info("Verificando si existe usuario: {}", usuario);
        
        try {
            boolean existe = usuarioService.existePorUsuario(usuario);
            Map<String, Boolean> response = new HashMap<>();
            response.put("existe", existe);
            
            log.info("Usuario '{}' existe: {}", usuario, existe);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al verificar usuario: {}", usuario, e);
            Map<String, Boolean> response = new HashMap<>();
            response.put("existe", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Endpoint de login (placeholder para futura implementación)
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        log.info("Intento de login para usuario: {}", request.getUsuario());
        
        // TODO: Implementar lógica de autenticación real
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("mensaje", "Autenticación no implementada aún");
        
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(response);
    }

    // Clase DTO para el request de login
    public static class LoginRequest {
        private String usuario;
        private String contrasena;

        // Getters y Setters
        public String getUsuario() { return usuario; }
        public void setUsuario(String usuario) { this.usuario = usuario; }
        
        public String getContrasena() { return contrasena; }
        public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    }
}
