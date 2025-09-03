@echo off
echo 🔧 Descargando dependencias y verificando proyecto CRM...
echo.

echo 1. Limpiando proyecto...
call mvn clean

echo.
echo 2. Descargando dependencias...
call mvn dependency:resolve

echo.
echo 3. Compilando proyecto...
call mvn compile

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ✅ ¡Proyecto listo!
    echo 🚀 Todas las dependencias están correctas
    echo.
    echo Para ejecutar: mvn spring-boot:run
    echo O usar: clean-and-run.bat
) else (
    echo.
    echo ❌ Error en la compilación
    echo Revisa los errores arriba
)

echo.
echo 📋 Dependencias principales:
echo - Spring Boot Web
echo - Spring Boot Data JPA
echo - H2 Database
echo - MySQL Connector
echo - Spring Boot DevTools
echo.

pause
