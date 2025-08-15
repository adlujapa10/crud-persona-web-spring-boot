package com.gestioncrm.repository;

import com.gestioncrm.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Persona
 * Proporciona métodos para acceder a la base de datos
 */
@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {

    /**
     * Buscar persona por cédula
     */
    Optional<Persona> findByCedula(String cedula);

    /**
     * Verificar si existe una persona con la cédula dada
     */
    boolean existsByCedula(String cedula);

    /**
     * Buscar personas por nombre (ignorando mayúsculas/minúsculas)
     */
    List<Persona> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Buscar personas por apellido (ignorando mayúsculas/minúsculas)
     */
    List<Persona> findByApellidoContainingIgnoreCase(String apellido);

    /**
     * Buscar personas por rol
     */
    List<Persona> findByRol(String rol);

    /**
     * Buscar personas por sexo
     */
    List<Persona> findBySexo(Persona.Sexo sexo);

    /**
     * Buscar personas que tengan email
     */
    @Query("SELECT p FROM Persona p WHERE p.email IS NOT NULL AND p.email != ''")
    List<Persona> findPersonasConEmail();

    /**
     * Buscar personas que tengan teléfono
     */
    @Query("SELECT p FROM Persona p WHERE p.telefono IS NOT NULL AND p.telefono != ''")
    List<Persona> findPersonasConTelefono();

    /**
     * Buscar personas por rango de edad
     */
    @Query("SELECT p FROM Persona p WHERE p.edad BETWEEN :edadMin AND :edadMax")
    List<Persona> findByEdadBetween(@Param("edadMin") Integer edadMin, @Param("edadMax") Integer edadMax);

    /**
     * Contar personas por rol
     */
    @Query("SELECT p.rol, COUNT(p) FROM Persona p WHERE p.rol IS NOT NULL GROUP BY p.rol")
    List<Object[]> countByRol();

    /**
     * Contar personas por sexo
     */
    @Query("SELECT p.sexo, COUNT(p) FROM Persona p WHERE p.sexo IS NOT NULL GROUP BY p.sexo")
    List<Object[]> countBySexo();



    /**
     * Buscar personas ordenadas por nombre
     */
    List<Persona> findAllByOrderByNombreAsc();

    /**
     * Buscar personas que coincidan con nombre o apellido
     */
    @Query("SELECT p FROM Persona p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR LOWER(p.apellido) LIKE LOWER(CONCAT('%', :termino, '%'))")
    List<Persona> findByNombreOrApellidoContaining(@Param("termino") String termino);
}
