#!/bin/bash
echo "🔧 Descargando dependencias y verificando proyecto CRM..."
echo

echo "1. Limpiando proyecto..."
mvn clean

echo
echo "2. Descargando dependencias..."
mvn dependency:resolve

echo
echo "3. Compilando proyecto..."
mvn compile

if [ $? -eq 0 ]; then
    echo
    echo "✅ ¡Proyecto listo!"
    echo "🚀 Todas las dependencias están correctas"
    echo
    echo "Para ejecutar: mvn spring-boot:run"
    echo "O usar: ./clean-and-run.sh"
else
    echo
    echo "❌ Error en la compilación"
    echo "Revisa los errores arriba"
fi

echo
echo "📋 Dependencias principales:"
echo "- Spring Boot Web"
echo "- Spring Boot Data JPA"
echo "- H2 Database"
echo "- MySQL Connector"
echo "- Spring Boot DevTools"
echo
