@echo off
echo ========================================
echo VERIFICANDO CONEXION A MYSQL
echo ========================================
echo.

echo Verificando que MySQL esté ejecutándose...
net start | findstr /i mysql

echo.
echo Intentando conectar a MySQL...
mysql -u root -p -e "SHOW DATABASES;" 2>nul
if %errorlevel% equ 0 (
    echo ✅ MySQL está funcionando correctamente
    echo.
    echo Creando base de datos si no existe...
    mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS gestioncrm;"
    echo ✅ Base de datos 'gestioncrm' lista
) else (
    echo ❌ Error al conectar a MySQL
    echo.
    echo Por favor verifica:
    echo 1. MySQL esté instalado y ejecutándose
    echo 2. Usuario 'root' con password 'password'
    echo 3. Puerto 3306 esté disponible
)

echo.
pause
