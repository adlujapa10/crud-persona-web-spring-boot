@echo off
echo 🔍 Verificando compilación del proyecto CRM...
echo.

echo 1. Limpiando proyecto...
call mvn clean

echo.
echo 2. Compilando proyecto...
call mvn compile

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ✅ Compilación exitosa!
    echo 🚀 El proyecto está listo para ejecutar
    echo.
    echo Para ejecutar: mvn spring-boot:run
    echo O usar: clean-and-run.bat
) else (
    echo.
    echo ❌ Error en la compilación
    echo Revisa los errores arriba
)

pause
