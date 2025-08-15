-- Script para configurar la base de datos MySQL
-- Ejecutar este script en MySQL Workbench o en la línea de comandos de MySQL

-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS gestioncrm;

-- Usar la base de datos
USE gestioncrm;

-- Verificar que la base de datos se creó correctamente
SHOW DATABASES;

-- Verificar que estamos usando la base de datos correcta
SELECT DATABASE();

-- Nota: Las tablas se crearán automáticamente cuando ejecutes la aplicación Spring Boot
-- debido a la configuración: spring.jpa.hibernate.ddl-auto=create-drop
