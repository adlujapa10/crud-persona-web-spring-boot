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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import com.gestioncrm.repository.PersonaRepository;
import com.gestioncrm.model.Persona;

/**
 * Controlador para la gestión de usuarios
 */
@RestController
@RequestMapping("/usuarios/api")
@CrossOrigin
public class UsuarioController {

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);
    
    private final UsuarioService usuarioService;
    private final PersonaRepository personaRepository;

    public UsuarioController(UsuarioService usuarioService, PersonaRepository personaRepository) {
        this.usuarioService = usuarioService;
        this.personaRepository = personaRepository;
    }

    /**
     * Obtener todos los usuarios
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> listarUsuarios() {
        log.info("Obteniendo lista de usuarios");
        
        try {
            List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("usuarios", usuarios.stream().map(UsuarioResponse::new).collect(Collectors.toList()));
            response.put("total", usuarios.size());
            
            log.info("Se obtuvieron {} usuarios", usuarios.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al obtener usuarios", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("mensaje", "Error al obtener la lista de usuarios");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Obtener un usuario por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerUsuario(@PathVariable Long id) {
        log.info("Obteniendo usuario con ID: {}", id);
        
        try {
            Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(id);
            
            if (usuarioOpt.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("usuario", new UsuarioResponse(usuarioOpt.get()));
                
                log.info("Usuario encontrado: {}", usuarioOpt.get().getUsuario());
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("mensaje", "Usuario no encontrado");
                
                log.warn("Usuario no encontrado con ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            log.error("Error al obtener usuario", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("mensaje", "Error al obtener el usuario");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Crear un nuevo usuario
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearUsuario(@Valid @RequestBody UsuarioCreateRequest request) {
        log.info("Creando nuevo usuario: {}", request.getUsuario());
        
        try {
            // Verificar si el usuario ya existe
            if (usuarioService.existePorUsuario(request.getUsuario())) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("mensaje", "El nombre de usuario ya existe");
                
                log.warn("Intento de crear usuario duplicado: {}", request.getUsuario());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            // Validar campos obligatorios
            if (request.getPersonaId() == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("mensaje", "El ID de la persona es obligatorio");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            // Verificar que la persona existe
            if (!personaRepository.existsById(request.getPersonaId())) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("mensaje", "La persona especificada no existe");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            // Verificar que la persona no tenga ya un usuario
            if (usuarioService.obtenerPorPersonaId(request.getPersonaId()).isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("mensaje", "La persona ya tiene un usuario asociado");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
            
            Usuario nuevoUsuario = usuarioService.crearUsuario(request);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("mensaje", "Usuario creado exitosamente");
            response.put("usuario", new UsuarioResponse(nuevoUsuario));
            
            log.info("Usuario creado exitosamente: {}", nuevoUsuario.getUsuario());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            log.error("Error de validación al crear usuario: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("mensaje", "Error de validación: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            log.error("Error al crear usuario", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("mensaje", "Error interno del servidor al crear el usuario");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Actualizar un usuario existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarUsuario(
            @PathVariable Long id, 
            @Valid @RequestBody UsuarioUpdateRequest request) {
        log.info("Actualizando usuario con ID: {}", id);
        
        try {
            Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(id);
            
            if (usuarioOpt.isPresent()) {
                Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, request);
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("mensaje", "Usuario actualizado exitosamente");
                response.put("usuario", new UsuarioResponse(usuarioActualizado));
                
                log.info("Usuario actualizado exitosamente: {}", usuarioActualizado.getUsuario());
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("mensaje", "Usuario no encontrado");
                
                log.warn("Usuario no encontrado para actualizar con ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            log.error("Error al actualizar usuario", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("mensaje", "Error al actualizar el usuario");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Eliminar un usuario
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarUsuario(@PathVariable Long id) {
        log.info("Eliminando usuario con ID: {}", id);
        
        try {
            Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(id);
            
            if (usuarioOpt.isPresent()) {
                usuarioService.eliminarUsuario(id);
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("mensaje", "Usuario eliminado exitosamente");
                
                log.info("Usuario eliminado exitosamente: {}", usuarioOpt.get().getUsuario());
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("mensaje", "Usuario no encontrado");
                
                log.warn("Usuario no encontrado para eliminar con ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            log.error("Error al eliminar usuario", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("mensaje", "Error al eliminar el usuario");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Buscar usuarios por nombre de usuario
     */
    @GetMapping("/buscar")
    public ResponseEntity<Map<String, Object>> buscarUsuarios(@RequestParam String termino) {
        log.info("Buscando usuarios con término: {}", termino);
        
        try {
            List<Usuario> usuarios = usuarioService.buscarPorUsuario(termino);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("usuarios", usuarios.stream().map(UsuarioResponse::new).collect(Collectors.toList()));
            response.put("total", usuarios.size());
            
            log.info("Se encontraron {} usuarios", usuarios.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al buscar usuarios", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("mensaje", "Error al buscar usuarios");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Obtener estadísticas de usuarios
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        log.info("Obteniendo estadísticas de usuarios");
        
        try {
            Map<String, Object> estadisticas = usuarioService.obtenerEstadisticas();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("estadisticas", estadisticas);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al obtener estadísticas", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("mensaje", "Error al obtener estadísticas");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Obtener personas disponibles para crear usuarios (sin usuario asociado)
     */
    @GetMapping("/personas-disponibles")
    public ResponseEntity<Map<String, Object>> obtenerPersonasDisponibles() {
        log.info("Obteniendo personas disponibles para crear usuarios");
        
        try {
            List<Persona> personasDisponibles = personaRepository.findAll().stream()
                    .filter(persona -> !usuarioService.obtenerPorPersonaId(persona.getId()).isPresent())
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("personas", personasDisponibles);
            response.put("total", personasDisponibles.size());
            
            log.info("Se encontraron {} personas disponibles", personasDisponibles.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al obtener personas disponibles", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("mensaje", "Error al obtener personas disponibles");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Clases DTO para requests y responses
    public static class UsuarioResponse {
        private Long id;
        private String usuario;
        private String nombreCompleto;
        private String email;
        private String rol;

        public UsuarioResponse(Usuario usuario) {
            this.id = usuario.getId();
            this.usuario = usuario.getUsuario();
            if (usuario.getPersona() != null) {
                this.nombreCompleto = usuario.getPersona().getNombreCompleto();
                this.email = usuario.getPersona().getEmail();
            }
            // Por ahora no tenemos rol, pero podemos agregarlo después
            this.rol = "Usuario";
        }

        // Getters
        public Long getId() { return id; }
        public String getUsuario() { return usuario; }
        public String getNombreCompleto() { return nombreCompleto; }
        public String getEmail() { return email; }
        public String getRol() { return rol; }
    }

    public static class UsuarioCreateRequest {
        private String usuario;
        private String contrasena;
        private Long personaId; // ID de la persona existente

        // Getters y Setters
        public String getUsuario() { return usuario; }
        public void setUsuario(String usuario) { this.usuario = usuario; }
        
        public String getContrasena() { return contrasena; }
        public void setContrasena(String contrasena) { this.contrasena = contrasena; }
        
        public Long getPersonaId() { return personaId; }
        public void setPersonaId(Long personaId) { this.personaId = personaId; }
    }

    public static class UsuarioUpdateRequest {
        private String usuario;
        private String contrasena;
        private Long personaId; // ID de la persona existente

        // Getters y Setters
        public String getUsuario() { return usuario; }
        public void setUsuario(String usuario) { this.usuario = usuario; }
        
        public String getContrasena() { return contrasena; }
        public void setContrasena(String contrasena) { this.contrasena = contrasena; }
        
        public Long getPersonaId() { return personaId; }
        public void setPersonaId(Long personaId) { this.personaId = personaId; }
    }
}
