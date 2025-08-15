package com.gestioncrm.model;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Entidad Persona que representa una persona en el sistema CRM
 * Mapeada a la tabla 'persona' en la base de datos
 */
@Entity
@Table(name = "persona")
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    @NotBlank(message = "La cédula es obligatoria")
    @Size(min = 5, max = 20, message = "La cédula debe tener entre 5 y 20 caracteres")
    @Column(name = "cedula", nullable = false, unique = true, length = 20)
    private String cedula;

    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    @Pattern(regexp = "^[+]?[0-9\\s\\-()]+$", message = "Formato de teléfono inválido")
    @Column(name = "telefono", length = 20)
    private String telefono;

    @Email(message = "Formato de email inválido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    @Column(name = "email", length = 100)
    private String email;

    @Min(value = 1, message = "La edad debe ser mayor a 0")
    @Max(value = 120, message = "La edad no puede exceder 120 años")
    @Column(name = "edad")
    private Integer edad;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo", length = 1)
    private Sexo sexo;

    @Size(max = 50, message = "El rol no puede exceder 50 caracteres")
    @Column(name = "rol", length = 50)
    private String rol;

    /**
     * Enum para el sexo de la persona
     */
    public enum Sexo {
        M("Masculino"),
        F("Femenino");

        private final String descripcion;

        Sexo(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    // Constructores
    public Persona() {
    }

    public Persona(String nombre, String apellido, String cedula, String telefono, 
                   String email, Integer edad, Sexo sexo, String rol) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.telefono = telefono;
        this.email = email;
        this.edad = edad;
        this.sexo = sexo;
        this.rol = rol;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    /**
     * Método para obtener el nombre completo
     */
    @Transient
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    /**
     * Método para verificar si la persona tiene email
     */
    @Transient
    public boolean tieneEmail() {
        return email != null && !email.trim().isEmpty();
    }

    /**
     * Método para verificar si la persona tiene teléfono
     */
    @Transient
    public boolean tieneTelefono() {
        return telefono != null && !telefono.trim().isEmpty();
    }

    /**
     * Método para verificar si la persona tiene rol
     */
    @Transient
    public boolean tieneRol() {
        return rol != null && !rol.trim().isEmpty();
    }



    @Override
    public String toString() {
        return "Persona{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", cedula='" + cedula + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", edad=" + edad +
                ", sexo=" + sexo +
                ", rol='" + rol + '\'' +
                '}';
    }
}
