@echo off
echo ========================================
echo LIMPIANDO Y EJECUTANDO PROYECTO SPRING
echo ========================================
echo.

echo 1. Limpiando proyecto...
call mvn clean

echo.
echo 2. Compilando proyecto...
call mvn compile

echo.
echo 3. Ejecutando aplicación...
echo URL: http://localhost:8081/crm
echo.
echo.
call mvn spring-boot:run

pause
