#!/bin/bash
echo "🧹 Limpiando proyecto y ejecutando aplicación CRM..."
echo

echo "1. Limpiando target..."
rm -rf target

echo "2. Limpiando cache de Maven..."
mvn clean

echo "3. Compilando proyecto..."
mvn compile

echo "4. Ejecutando aplicación..."
echo
echo "🚀 Iniciando Sistema CRM..."
echo "📍 URL: http://localhost:8081/crm"
echo "🗄️  H2 Console: http://localhost:8081/crm/h2-console"
echo
mvn spring-boot:run
