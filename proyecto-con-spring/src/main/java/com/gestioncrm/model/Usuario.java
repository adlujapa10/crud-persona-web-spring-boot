package com.gestioncrm.model;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Entidad Usuario que representa un usuario del sistema
 * Mapeada a la tabla 'usuario' en la base de datos
 */
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @NotNull(message = "La persona es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona", nullable = false)
    private Persona persona;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 45, message = "El nombre de usuario debe tener entre 3 y 45 caracteres")
    @Column(name = "usuario", nullable = false, unique = true, length = 45)
    private String usuario;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 4, max = 45, message = "La contraseña debe tener entre 4 y 45 caracteres")
    @Column(name = "contrasena", nullable = false, length = 45)
    private String contrasena;

    // Constructores
    public Usuario() {
    }

    public Usuario(Persona persona, String usuario, String contrasena) {
        this.persona = persona;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

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

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", usuario='" + usuario + '\'' +
                ", persona=" + (persona != null ? persona.getNombreCompleto() : "null") +
                '}';
    }
}
