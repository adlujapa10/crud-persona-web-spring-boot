package com.gestioncrm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Spring Boot
 * Sistema de Gestión CRM con Spring Boot y Hibernate
 */
@SpringBootApplication
public class GestionCrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionCrmApplication.class, args);
        System.out.println("🚀 Sistema CRM iniciado en: http://localhost:8081/crm");
    }
}
