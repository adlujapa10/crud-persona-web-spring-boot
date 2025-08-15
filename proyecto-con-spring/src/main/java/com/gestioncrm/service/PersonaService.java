package com.gestioncrm.service;

import com.gestioncrm.model.Persona;
import com.gestioncrm.repository.PersonaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Set;

/**
 * Servicio de negocio para la gestión de personas
 * Contiene la lógica de negocio y transacciones
 */
@Service
@Transactional
public class PersonaService {

    private static final Logger log = LoggerFactory.getLogger(PersonaService.class);
    
    private final PersonaRepository personaRepository;

    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    /**
     * Obtener todas las personas ordenadas por nombre
     */
    @Transactional(readOnly = true)
    public List<Persona> obtenerTodas() {
        log.info("Obteniendo todas las personas");
        return personaRepository.findAllByOrderByNombreAsc();
    }

    /**
     * Obtener una persona por su ID
     */
    @Transactional(readOnly = true)
    public Persona obtenerPorId(Long id) {
        log.info("Buscando persona con ID: {}", id);
        return personaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con ID: " + id));
    }

    /**
     * Obtener una persona por su cédula
     */
    @Transactional(readOnly = true)
    public Optional<Persona> obtenerPorCedula(String cedula) {
        log.info("Buscando persona con cédula: {}", cedula);
        return personaRepository.findByCedula(cedula);
    }

    /**
     * Guardar una nueva persona
     */
    public Persona guardar(Persona persona) {
        log.info("Guardando nueva persona: {}", persona.getNombreCompleto());
        
        // Verificar si ya existe una persona con la misma cédula
        if (personaRepository.existsByCedula(persona.getCedula())) {
            throw new IllegalArgumentException("Ya existe una persona con la cédula: " + persona.getCedula());
        }
        
        return personaRepository.save(persona);
    }

    /**
     * Actualizar una persona existente
     */
    public Persona actualizar(Long id, Persona persona) {
        log.info("Actualizando persona con ID: {}", id);
        
        // Verificar que la persona existe
        Persona personaExistente = obtenerPorId(id);
        
        // Verificar si la cédula ya existe en otra persona
        Optional<Persona> personaConCedula = personaRepository.findByCedula(persona.getCedula());
        if (personaConCedula.isPresent() && !personaConCedula.get().getId().equals(id)) {
            throw new IllegalArgumentException("Ya existe otra persona con la cédula: " + persona.getCedula());
        }
        
        // Actualizar campos
        personaExistente.setNombre(persona.getNombre());
        personaExistente.setApellido(persona.getApellido());
        personaExistente.setCedula(persona.getCedula());
        personaExistente.setTelefono(persona.getTelefono());
        personaExistente.setEmail(persona.getEmail());
        personaExistente.setEdad(persona.getEdad());
        personaExistente.setSexo(persona.getSexo());
        personaExistente.setRol(persona.getRol());
        
        return personaRepository.save(personaExistente);
    }

    /**
     * Eliminar una persona por su ID
     */
    public void eliminar(Long id) {
        log.info("Eliminando persona con ID: {}", id);
        
        // Verificar que la persona existe
        if (!personaRepository.existsById(id)) {
            throw new EntityNotFoundException("Persona no encontrada con ID: " + id);
        }
        
        personaRepository.deleteById(id);
    }

    /**
     * Buscar personas por término (nombre o apellido)
     */
    @Transactional(readOnly = true)
    public List<Persona> buscarPorTermino(String termino) {
        log.info("Buscando personas con término: {}", termino);
        return personaRepository.findByNombreOrApellidoContaining(termino);
    }

    /**
     * Buscar personas por rol
     */
    @Transactional(readOnly = true)
    public List<Persona> buscarPorRol(String rol) {
        log.info("Buscando personas con rol: {}", rol);
        return personaRepository.findByRol(rol);
    }

    /**
     * Buscar personas por sexo
     */
    @Transactional(readOnly = true)
    public List<Persona> buscarPorSexo(Persona.Sexo sexo) {
        log.info("Buscando personas con sexo: {}", sexo);
        return personaRepository.findBySexo(sexo);
    }

    /**
     * Obtener estadísticas de personas
     */
    @Transactional(readOnly = true)
    public EstadisticasPersonas obtenerEstadisticas() {
        log.info("Obteniendo estadísticas de personas");
        
        long totalPersonas = personaRepository.count();
        long personasConEmail = personaRepository.findPersonasConEmail().size();
        long personasConTelefono = personaRepository.findPersonasConTelefono().size();
        long personasConRol = personaRepository.findAll().stream()
                .filter(Persona::tieneRol)
                .count();
        
        return new EstadisticasPersonas(totalPersonas, personasConEmail, personasConTelefono, personasConRol);
    }

    /**
     * Verificar si existe una persona con la cédula dada
     */
    @Transactional(readOnly = true)
    public boolean existePorCedula(String cedula) {
        return personaRepository.existsByCedula(cedula);
    }

    /**
     * Limpiar datos duplicados por cédula
     */
    @Transactional
    public void limpiarDuplicados() {
        log.info("Limpiando datos duplicados...");
        
        List<Persona> todasLasPersonas = personaRepository.findAll();
        Set<String> cedulasVistas = new HashSet<>();
        List<Persona> personasAEliminar = new ArrayList<>();
        
        for (Persona persona : todasLasPersonas) {
            if (cedulasVistas.contains(persona.getCedula())) {
                personasAEliminar.add(persona);
                log.warn("Persona duplicada encontrada: ID={}, Cédula={}, Nombre={}", 
                        persona.getId(), persona.getCedula(), persona.getNombreCompleto());
            } else {
                cedulasVistas.add(persona.getCedula());
            }
        }
        
        if (!personasAEliminar.isEmpty()) {
            personaRepository.deleteAll(personasAEliminar);
            log.info("Eliminadas {} personas duplicadas", personasAEliminar.size());
        } else {
            log.info("No se encontraron duplicados");
        }
    }

    /**
     * Clase para encapsular estadísticas de personas
     */
    public static class EstadisticasPersonas {
        private final long totalPersonas;
        private final long personasConEmail;
        private final long personasConTelefono;
        private final long personasConRol;

        public EstadisticasPersonas(long totalPersonas, long personasConEmail, 
                                  long personasConTelefono, long personasConRol) {
            this.totalPersonas = totalPersonas;
            this.personasConEmail = personasConEmail;
            this.personasConTelefono = personasConTelefono;
            this.personasConRol = personasConRol;
        }

        // Getters
        public long getTotalPersonas() { return totalPersonas; }
        public long getPersonasConEmail() { return personasConEmail; }
        public long getPersonasConTelefono() { return personasConTelefono; }
        public long getPersonasConRol() { return personasConRol; }
    }
}
