@echo off
echo Configurando y ejecutando proyecto Spring Boot en Windows...
echo.

REM Configurar perfil de Spring Boot
set SPRING_PROFILES_ACTIVE=windows

REM Limpiar y compilar
echo Limpiando proyecto...
call mvn clean

echo Compilando proyecto...
call mvn compile

echo Ejecutando aplicación...
call mvn spring-boot:run -Dspring.profiles.active=test

pause
