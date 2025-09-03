-- Archivo de importación de datos para Hibernate
-- Este archivo se ejecuta automáticamente después de crear las tablas

-- Insertar personas de prueba
INSERT INTO persona (nombre, apellido, cedula, email, telefono, sexo, edad, rol) VALUES ('Juan', 'Pérez', '1234567890', 'juan.perez@email.com', '3001234567', 'M', 33, 'Cliente');
INSERT INTO persona (nombre, apellido, cedula, email, telefono, sexo, edad, rol) VALUES ('María', 'González', '0987654321', 'maria.gonzalez@email.com', '3109876543', 'F', 38, 'Cliente');
INSERT INTO persona (nombre, apellido, cedula, email, telefono, sexo, edad, rol) VALUES ('Carlos', 'Rodríguez', '1122334455', 'carlos.rodriguez@email.com', '3155551234', 'M', 31, 'Cliente');
INSERT INTO persona (nombre, apellido, cedula, email, telefono, sexo, edad, rol) VALUES ('Ana', 'López', '5566778899', 'ana.lopez@email.com', '3207778888', 'F', 35, 'Cliente');
INSERT INTO persona (nombre, apellido, cedula, email, telefono, sexo, edad, rol) VALUES ('Pedro', 'Martínez', '9988776655', 'pedro.martinez@email.com', '3009990000', 'M', 28, 'Cliente');

-- Insertar usuarios de prueba
INSERT INTO usuario (id_persona, usuario, contrasena) VALUES (1, 'juan.perez', 'password123');
INSERT INTO usuario (id_persona, usuario, contrasena) VALUES (2, 'maria.gonzalez', 'password123');
INSERT INTO usuario (id_persona, usuario, contrasena) VALUES (3, 'admin', 'admin123');
