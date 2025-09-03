@echo off
echo 🧹 Limpiando proyecto y ejecutando aplicación CRM...
echo.

echo 1. Limpiando target...
if exist target rmdir /s /q target

echo 2. Limpiando cache de Maven...
call mvn clean

echo 3. Compilando proyecto...
call mvn compile

echo 4. Ejecutando aplicación...
echo.
echo 🚀 Iniciando Sistema CRM...
echo 📍 URL: http://localhost:8081/crm
echo 🗄️  H2 Console: http://localhost:8081/crm/h2-console
echo.
call mvn spring-boot:run

pause
