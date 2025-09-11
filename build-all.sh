#!/bin/bash
set -e

echo "=== Construyendo UI ==="
cd web-ui

# Instalar dependencias y construir
if [ -f yarn.lock ]; then
  echo "Usando Yarn..."
  yarn install
  yarn build
else
  echo "Usando npm..."
  npm install
  npm run build
fi

cd ..

# Copiar archivos al backend
echo "=== Copiando archivos estáticos al backend ==="
STATIC_DIR="web-server/src/main/resources/static"
rm -rf $STATIC_DIR
mkdir -p $STATIC_DIR
cp -r web-ui/dist/* $STATIC_DIR

# Construir el JAR con Gradle
echo "=== Construyendo JAR del backend ==="
cd web-server

if [ -f gradlew ]; then
  ./gradlew clean build
else
  gradle clean build
fi

echo "=== Build completado! JAR generado en web-server/build/libs/ ==="

