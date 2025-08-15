#!/bin/bash

echo "🧹 Limpiando proyecto..."
mvn clean

echo "🔨 Compilando proyecto..."
mvn compile

echo "🚀 Ejecutando aplicación..."
mvn spring-boot:run
