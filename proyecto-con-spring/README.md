# Sistema de Gestión CRM con Spring Boot

Un sistema web moderno desarrollado con Spring Boot, Hibernate/JPA y Thymeleaf para la gestión de personas siguiendo las mejores prácticas de desarrollo.

## 🚀 Características

- **Spring Boot 2.7.14**: Framework moderno y robusto
- **Hibernate/JPA**: ORM para gestión de base de datos
- **Thymeleaf**: Motor de plantillas para vistas
- **MySQL**: Base de datos relacional
- **Bootstrap 5**: Interfaz moderna y responsive
- **Validación**: Validación automática de formularios
- **Logging**: Sistema de logs completo
- **Arquitectura MVC**: Separación clara de responsabilidades

## 🛠️ Tecnologías Utilizadas

- **Backend**: Spring Boot 2.7.14, Spring Data JPA, Spring Validation
- **Base de Datos**: MySQL 8.0, Hibernate
- **Frontend**: Thymeleaf, Bootstrap 5, Font Awesome, JavaScript
- **Build Tool**: Maven
- **Lombok**: Reducción de código boilerplate

## 📋 Requisitos Previos

- Java JDK 11 o superior
- MySQL 8.0 o superior
- Maven 3.6 o superior

## 🔧 Instalación

### 1. Clonar el Proyecto

```bash
cd proyecto-con-spring
```

### 2. Configurar la Base de Datos

1. **Crear base de datos** en MySQL:
```sql
CREATE DATABASE gestioncrm CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. **Configurar credenciales** en `src/main/resources/application.properties`:
```properties
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```

### 3. Ejecutar la Aplicación

```bash
# Compilar y ejecutar
mvn spring-boot:run

# O compilar y ejecutar el JAR
mvn clean package
java -jar target/gestioncrm-spring-1.0.0.jar
```

### 4. Acceder a la Aplicación

- **URL**: http://localhost:8081/crm
- **Puerto**: 8081 (configurable en application.properties)

## 📁 Estructura del Proyecto

```
proyecto-con-spring/
├── src/
│   └── main/
│       ├── java/com/gestioncrm/
│       │   ├── GestionCrmApplication.java
│       │   ├── model/
│       │   │   └── Persona.java
│       │   ├── repository/
│       │   │   └── PersonaRepository.java
│       │   ├── service/
│       │   │   └── PersonaService.java
│       │   └── controller/
│       │       ├── HomeController.java
│       │       └── PersonaController.java
│       └── resources/
│           ├── application.properties
│           └── templates/
│               ├── home.html
│               ├── personas/
│               │   ├── lista.html
│               │   └── formulario.html
│               └── layout/
│                   └── base.html
├── pom.xml
└── README.md
```

## 🎯 Funcionalidades

### Gestión de Personas

- **Listar Personas**: Ver todas las personas con estadísticas
- **Agregar Persona**: Formulario con validación automática
- **Editar Persona**: Modificar información existente
- **Eliminar Persona**: Eliminar con confirmación
- **Buscar Personas**: Búsqueda por nombre o apellido
- **Filtrar**: Por rol, sexo, etc.

### Campos de la Persona

- **ID**: Identificador único (auto-incrementable)
- **Nombre**: Nombre de la persona (obligatorio, 2-100 caracteres)
- **Apellido**: Apellido de la persona (obligatorio, 2-100 caracteres)
- **Cédula**: Número de identificación único (obligatorio, 5-20 caracteres)
- **Teléfono**: Número de contacto (opcional, formato validado)
- **Email**: Dirección de correo electrónico (opcional, formato validado)
- **Edad**: Edad de la persona (opcional, 1-120 años)
- **Sexo**: Masculino o Femenino (opcional)
- **Rol**: Rol o función de la persona (opcional, 50 caracteres)
- **Fechas**: Creación y actualización automáticas

## 🔒 Seguridad y Validación

- **Validación de Entrada**: Anotaciones Bean Validation
- **Validación de Negocio**: Lógica en capa de servicio
- **Transacciones**: Gestión automática de transacciones
- **Logging**: Registro de todas las operaciones
- **Manejo de Errores**: Páginas de error personalizadas

## 🎨 Interfaz de Usuario

- **Diseño Responsive**: Adaptable a diferentes dispositivos
- **Bootstrap 5**: Framework CSS moderno
- **Font Awesome**: Iconos profesionales
- **Thymeleaf**: Plantillas dinámicas
- **Validación Visual**: Feedback inmediato al usuario

## 🚀 URLs del Sistema

- **Página Principal**: `/crm/`
- **Lista de Personas**: `/crm/personas`
- **Nueva Persona**: `/crm/personas/nueva`
- **Editar Persona**: `/crm/personas/editar/{id}`
- **Eliminar Persona**: `/crm/personas/eliminar/{id}`
- **Buscar Personas**: `/crm/personas/buscar?termino={termino}`
- **Filtrar por Rol**: `/crm/personas/filtrar/rol/{rol}`
- **Filtrar por Sexo**: `/crm/personas/filtrar/sexo/{sexo}`

## 🔧 Configuración

### Base de Datos

```properties
# Configuración MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/gestioncrm?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=password

# Configuración JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### Servidor

```properties
# Puerto y contexto
server.port=8081
server.servlet.context-path=/crm
```

### Thymeleaf

```properties
# Configuración de plantillas
spring.thymeleaf.cache=false
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
```

## 📊 Características Avanzadas

### Repositorio JPA

- **Métodos Personalizados**: Búsquedas específicas
- **Consultas JPQL**: Consultas optimizadas
- **Ordenamiento**: Múltiples criterios de orden
- **Filtros**: Búsqueda por diferentes campos

### Servicio de Negocio

- **Transacciones**: Gestión automática
- **Validación de Negocio**: Reglas específicas
- **Estadísticas**: Métricas del sistema
- **Logging**: Registro detallado de operaciones

### Controlador

- **Manejo de Errores**: Gestión de excepciones
- **Validación**: Validación de formularios
- **Redirecciones**: Navegación fluida
- **Mensajes**: Feedback al usuario

## 🐛 Solución de Problemas

### Error de Conexión a Base de Datos

1. **Verificar** que MySQL esté ejecutándose
2. **Confirmar** credenciales en `application.properties`
3. **Verificar** que la base de datos `gestioncrm` exista

### Error de Compilación

1. **Verificar** que Java 11+ esté instalado
2. **Confirmar** que Maven esté configurado
3. **Ejecutar** `mvn clean compile`

### Error de Puerto

1. **Cambiar puerto** en `application.properties`
2. **Verificar** que el puerto esté libre
3. **Reiniciar** la aplicación

## 🚀 Despliegue

### Desarrollo

```bash
mvn spring-boot:run
```

### Producción

```bash
# Compilar
mvn clean package

# Ejecutar JAR
java -jar target/gestioncrm-spring-1.0.0.jar

# Con parámetros
java -jar target/gestioncrm-spring-1.0.0.jar --spring.profiles.active=prod
```

### Docker (Opcional)

```dockerfile
FROM openjdk:11-jre-slim
COPY target/gestioncrm-spring-1.0.0.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 📝 Licencia

Este proyecto está bajo la Licencia MIT.

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor:

1. Fork el proyecto
2. Crear una rama para tu feature
3. Commit tus cambios
4. Push a la rama
5. Abrir un Pull Request

## 📞 Soporte

Para soporte técnico o preguntas:

- Crear un issue en el repositorio
- Contactar al equipo de desarrollo

---

**Desarrollado con ❤️ usando Spring Boot, Hibernate y Thymeleaf**
