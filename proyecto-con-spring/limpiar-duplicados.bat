@echo off
echo ========================================
echo LIMPIANDO DATOS DUPLICADOS
echo ========================================
echo.

echo Enviando solicitud para limpiar duplicados...
curl -X POST http://localhost:8081/crm/personas/limpiar-duplicados

echo.
echo.
echo Verificando resultado...
echo URL: http://localhost:8081/crm/personas
echo.

pause
