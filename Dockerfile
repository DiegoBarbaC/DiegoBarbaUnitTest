# Usar una imagen base de Ubuntu con OpenJDK 17 y Maven
FROM ubuntu:20.04

# Instalar OpenJDK 17 y Maven
RUN apt-get update && apt-get install -y openjdk-17-jdk maven

# Instalar herramientas necesarias
RUN apt-get install -y \
    wget \
    curl \
    unzip \
    libx11-dev \
    libxkbfile1 \
    libsecret-1-0 \
    libnss3 \
    libgdk-pixbuf2.0-0 \
    libatk1.0-0 \
    libatk-bridge2.0-0 \
    libxcomposite1 \
    libxrandr2 \
    libasound2 \
    libxtst6 \
    libappindicator3-1 \
    libgconf-2-4

# Descargar Google Chrome
RUN wget -q https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb

# Instalar Google Chrome
RUN apt-get install -y ./google-chrome-stable_current_amd64.deb

# Limpiar archivos descargados
RUN rm ./google-chrome-stable_current_amd64.deb

# Limpiar cachés de apt
RUN rm -rf /var/lib/apt/lists/*

# Verificar instalación de Google Chrome
RUN google-chrome-stable --version

# Configurar el directorio de trabajo
WORKDIR /unittest

# Copiar el archivo pom.xml y descargar dependencias
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar todo el código fuente
COPY . .

# Exponer el puerto para la aplicación (en caso de que sea necesario)
EXPOSE 8080

# Comando de inicio: Ejecutar las pruebas
CMD mvn clean test
