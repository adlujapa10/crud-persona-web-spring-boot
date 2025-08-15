@echo off
echo ========================================
echo LIMPIEZA COMPLETA Y EJECUCION
echo ========================================
echo.

echo 1. Limpiando proyecto Maven...
call mvn clean

echo.
echo 2. Eliminando carpeta target...
if exist target rmdir /s /q target

echo.
echo 3. Compilando proyecto...
call mvn compile

echo.
echo 4. Ejecutando aplicación...
echo URL: http://localhost:8081/crm
echo.
call mvn spring-boot:run

pause
