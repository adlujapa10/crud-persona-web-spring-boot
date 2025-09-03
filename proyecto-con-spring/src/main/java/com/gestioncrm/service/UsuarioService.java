package com.gestioncrm.service;

import com.gestioncrm.model.Usuario;
import com.gestioncrm.model.Persona;
import com.gestioncrm.repository.UsuarioRepository;
import com.gestioncrm.repository.PersonaRepository;
import com.gestioncrm.controller.UsuarioController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Servicio de negocio para la gestión de usuarios
 * Contiene la lógica de negocio y transacciones
 */
@Service
@Transactional
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);
    
    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, PersonaRepository personaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.personaRepository = personaRepository;
    }

    /**
     * Autenticar usuario
     */
    @Transactional(readOnly = true)
    public Optional<Usuario> autenticar(String usuario, String contrasena) {
        log.info("Intentando autenticar usuario: {}", usuario);
        return usuarioRepository.findByUsuarioAndContrasena(usuario, contrasena);
    }

    /**
     * Obtener usuario por nombre de usuario
     */
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerPorUsuario(String usuario) {
        log.info("Buscando usuario: {}", usuario);
        return usuarioRepository.findByUsuario(usuario);
    }

    /**
     * Obtener usuario por ID
     */
    @Transactional(readOnly = true)
    public Usuario obtenerPorId(Long id) {
        log.info("Buscando usuario con ID: {}", id);
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
    }

    /**
     * Obtener usuario por ID (retorna Optional)
     */
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        log.info("Buscando usuario con ID: {}", id);
        return usuarioRepository.findById(id);
    }

    /**
     * Obtener todos los usuarios
     */
    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodos() {
        log.info("Obteniendo todos los usuarios");
        return usuarioRepository.findAll();
    }

    /**
     * Obtener todos los usuarios (alias para el controlador)
     */
    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodosLosUsuarios() {
        log.info("Obteniendo todos los usuarios");
        return usuarioRepository.findAll();
    }

    /**
     * Crear nuevo usuario
     */
    public Usuario crear(Usuario usuario) {
        log.info("Creando nuevo usuario: {}", usuario.getUsuario());
        
        // Verificar si ya existe un usuario con el mismo nombre de usuario
        if (usuarioRepository.existsByUsuario(usuario.getUsuario())) {
            throw new IllegalArgumentException("Ya existe un usuario con el nombre: " + usuario.getUsuario());
        }
        
        // Verificar que la persona existe
        if (usuario.getPersona() == null || usuario.getPersona().getId() == null) {
            throw new IllegalArgumentException("La persona es obligatoria");
        }
        
        Persona persona = personaRepository.findById(usuario.getPersona().getId())
                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con ID: " + usuario.getPersona().getId()));
        
        usuario.setPersona(persona);
        
        return usuarioRepository.save(usuario);
    }

    /**
     * Actualizar usuario
     */
    public Usuario actualizar(Long id, Usuario usuario) {
        log.info("Actualizando usuario con ID: {}", id);
        
        // Verificar que el usuario existe
        Usuario usuarioExistente = obtenerPorId(id);
        
        // Verificar si el nombre de usuario ya existe en otro usuario
        Optional<Usuario> usuarioConNombre = usuarioRepository.findByUsuario(usuario.getUsuario());
        if (usuarioConNombre.isPresent() && !usuarioConNombre.get().getId().equals(id)) {
            throw new IllegalArgumentException("Ya existe otro usuario con el nombre: " + usuario.getUsuario());
        }
        
        // Actualizar campos
        usuarioExistente.setUsuario(usuario.getUsuario());
        usuarioExistente.setContrasena(usuario.getContrasena());
        
        return usuarioRepository.save(usuarioExistente);
    }

    /**
     * Eliminar usuario
     */
    public void eliminar(Long id) {
        log.info("Eliminando usuario con ID: {}", id);
        
        // Verificar que el usuario existe
        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuario no encontrado con ID: " + id);
        }
        
        usuarioRepository.deleteById(id);
    }

    /**
     * Verificar si existe un usuario con el nombre de usuario dado
     */
    @Transactional(readOnly = true)
    public boolean existePorUsuario(String usuario) {
        return usuarioRepository.existsByUsuario(usuario);
    }

    /**
     * Buscar usuarios por nombre de usuario
     */
    @Transactional(readOnly = true)
    public List<Usuario> buscarPorUsuario(String termino) {
        log.info("Buscando usuarios con término: {}", termino);
        return usuarioRepository.findByUsuarioContainingIgnoreCase(termino);
    }

    /**
     * Crear usuario desde request del controlador
     */
    public Usuario crearUsuario(UsuarioController.UsuarioCreateRequest request) {
        log.info("Creando usuario desde request: {}", request.getUsuario());
        
        // Verificar que la persona existe
        if (request.getPersonaId() == null) {
            throw new IllegalArgumentException("El ID de la persona es obligatorio");
        }
        
        Persona persona = personaRepository.findById(request.getPersonaId())
                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con ID: " + request.getPersonaId()));
        
        // Verificar que la persona no tenga ya un usuario
        Optional<Usuario> usuarioExistente = usuarioRepository.findByPersonaId(request.getPersonaId());
        if (usuarioExistente.isPresent()) {
            throw new IllegalArgumentException("La persona ya tiene un usuario asociado");
        }
        
        // Crear el usuario
        Usuario usuario = new Usuario();
        usuario.setPersona(persona);
        usuario.setUsuario(request.getUsuario());
        usuario.setContrasena(request.getContrasena());
        
        return usuarioRepository.save(usuario);
    }

    /**
     * Actualizar usuario desde request del controlador
     */
    public Usuario actualizarUsuario(Long id, UsuarioController.UsuarioUpdateRequest request) {
        log.info("Actualizando usuario con ID: {} desde request", id);
        
        Usuario usuarioExistente = obtenerPorId(id);
        
        // Si se quiere cambiar la persona asociada, verificar que existe
        if (request.getPersonaId() != null && !request.getPersonaId().equals(usuarioExistente.getPersona().getId())) {
            Persona nuevaPersona = personaRepository.findById(request.getPersonaId())
                    .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con ID: " + request.getPersonaId()));
            
            // Verificar que la nueva persona no tenga ya un usuario
            Optional<Usuario> usuarioConNuevaPersona = usuarioRepository.findByPersonaId(request.getPersonaId());
            if (usuarioConNuevaPersona.isPresent()) {
                throw new IllegalArgumentException("La persona ya tiene un usuario asociado");
            }
            
            usuarioExistente.setPersona(nuevaPersona);
        }
        
        // Actualizar el usuario
        usuarioExistente.setUsuario(request.getUsuario());
        if (request.getContrasena() != null && !request.getContrasena().trim().isEmpty()) {
            usuarioExistente.setContrasena(request.getContrasena());
        }
        
        return usuarioRepository.save(usuarioExistente);
    }

    /**
     * Eliminar usuario (alias para el controlador)
     */
    public void eliminarUsuario(Long id) {
        eliminar(id);
    }

    /**
     * Obtener estadísticas de usuarios
     */
    @Transactional(readOnly = true)
    public Map<String, Object> obtenerEstadisticas() {
        log.info("Obteniendo estadísticas de usuarios");
        
        Map<String, Object> estadisticas = new HashMap<>();
        
        long totalUsuarios = usuarioRepository.count();
        long usuariosActivos = totalUsuarios; // Por ahora todos están activos
        
        estadisticas.put("totalUsuarios", totalUsuarios);
        estadisticas.put("usuariosActivos", usuariosActivos);
        estadisticas.put("usuariosInactivos", 0L);
        
        return estadisticas;
    }

    /**
     * Obtener usuario por ID de persona
     */
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerPorPersonaId(Long personaId) {
        log.info("Buscando usuario por ID de persona: {}", personaId);
        return usuarioRepository.findByPersonaId(personaId);
    }
}
