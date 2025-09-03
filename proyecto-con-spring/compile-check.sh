#!/bin/bash
echo "🔍 Verificando compilación del proyecto CRM..."
echo

echo "1. Limpiando proyecto..."
mvn clean

echo
echo "2. Compilando proyecto..."
mvn compile

if [ $? -eq 0 ]; then
    echo
    echo "✅ Compilación exitosa!"
    echo "🚀 El proyecto está listo para ejecutar"
    echo
    echo "Para ejecutar: mvn spring-boot:run"
    echo "O usar: ./clean-and-run.sh"
else
    echo
    echo "❌ Error en la compilación"
    echo "Revisa los errores arriba"
fi
