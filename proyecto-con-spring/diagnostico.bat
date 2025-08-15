@echo off
echo ========================================
echo DIAGNOSTICO DEL PROYECTO SPRING BOOT
echo ========================================
echo.

echo 1. Verificando Java...
java -version
echo.

echo 2. Verificando Maven...
mvn -version
echo.

echo 3. Verificando dependencias...
mvn dependency:tree | findstr mysql
echo.

echo 4. Limpiando proyecto...
mvn clean
echo.

echo 5. Compilando proyecto...
mvn compile
echo.

echo 6. Ejecutando con H2 (prueba)...
mvn spring-boot:run -Dspring.profiles.active=test
echo.

pause
