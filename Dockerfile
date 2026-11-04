# Etapa 1: Compilación del proyecto con Maven y Java 21
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder
WORKDIR /workspace

# Copiar configuración de dependencias para aprovechar el caché de capas de Docker
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente y compilar el archivo ejecutable JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Entorno de ejecución ligero con Java 21 JRE
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Crear usuario sin privilegios por directrices de seguridad
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copiar el archivo empaquetado desde la etapa previa
COPY --from=builder /workspace/target/*.jar app.jar

# Render asigna automáticamente el puerto en la variable de entorno $PORT
EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
