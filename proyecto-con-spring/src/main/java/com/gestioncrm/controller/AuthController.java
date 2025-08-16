package com.gestioncrm.controller;

import com.gestioncrm.model.Usuario;
import com.gestioncrm.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador para la autenticación de usuarios
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    
    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Login de usuario
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Intento de login para usuario: {}", loginRequest.getUsuario());
        
        try {
            Optional<Usuario> usuarioOpt = usuarioService.autenticar(loginRequest.getUsuario(), loginRequest.getContrasena());
            
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("mensaje", "Login exitoso");
                response.put("usuario", new UsuarioResponse(usuario));
                
                log.info("Login exitoso para usuario: {}", usuario.getUsuario());
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("mensaje", "Usuario o contraseña incorrectos");
                
                log.warn("Login fallido para usuario: {}", loginRequest.getUsuario());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            log.error("Error durante el login", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("mensaje", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Verificar si un usuario existe
     */
    @GetMapping("/verificar-usuario/{usuario}")
    public ResponseEntity<Map<String, Object>> verificarUsuario(@PathVariable String usuario) {
        log.info("Verificando existencia de usuario: {}", usuario);
        
        try {
            boolean existe = usuarioService.existePorUsuario(usuario);
            Map<String, Object> response = new HashMap<>();
            response.put("existe", existe);
            response.put("usuario", usuario);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al verificar usuario", e);
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Error al verificar usuario");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Clase para la solicitud de login
     */
    public static class LoginRequest {
        private String usuario;
        private String contrasena;

        // Constructores
        public LoginRequest() {}

        public LoginRequest(String usuario, String contrasena) {
            this.usuario = usuario;
            this.contrasena = contrasena;
        }

        // Getters y Setters
        public String getUsuario() {
            return usuario;
        }

        public void setUsuario(String usuario) {
            this.usuario = usuario;
        }

        public String getContrasena() {
            return contrasena;
        }

        public void setContrasena(String contrasena) {
            this.contrasena = contrasena;
        }
    }

    /**
     * Clase para la respuesta de usuario (sin contraseña)
     */
    public static class UsuarioResponse {
        private Long id;
        private String usuario;
        private String nombreCompleto;
        private String email;
        private String rol;

        public UsuarioResponse(Usuario usuario) {
            this.id = usuario.getId();
            this.usuario = usuario.getUsuario();
            this.nombreCompleto = usuario.getPersona().getNombreCompleto();
            this.email = usuario.getPersona().getEmail();
            this.rol = usuario.getPersona().getRol();
        }

        // Getters
        public Long getId() { return id; }
        public String getUsuario() { return usuario; }
        public String getNombreCompleto() { return nombreCompleto; }
        public String getEmail() { return email; }
        public String getRol() { return rol; }
    }
}
