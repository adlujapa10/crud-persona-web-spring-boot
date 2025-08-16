package com.gestioncrm.repository;

import com.gestioncrm.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad Usuario
 * Proporciona métodos para acceder a la base de datos
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Buscar usuario por nombre de usuario
     */
    Optional<Usuario> findByUsuario(String usuario);

    /**
     * Verificar si existe un usuario con el nombre de usuario dado
     */
    boolean existsByUsuario(String usuario);

    /**
     * Buscar usuario por nombre de usuario y contraseña
     */
    @Query("SELECT u FROM Usuario u WHERE u.usuario = :usuario AND u.contrasena = :contrasena")
    Optional<Usuario> findByUsuarioAndContrasena(@Param("usuario") String usuario, @Param("contrasena") String contrasena);

    /**
     * Buscar usuario por ID de persona
     */
    Optional<Usuario> findByPersonaId(Long personaId);
}
