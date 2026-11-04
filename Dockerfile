# Etapa 1: Compilación ultrarrápida del proyecto con Maven y Java 21
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder
WORKDIR /workspace

# Copiar configuración y código fuente para compilación directa
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -B

# Etapa 2: Entorno de ejecución ligero optimizado para Render
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Crear usuario seguro sin privilegios
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copiar el ejecutable JAR optimizado
COPY --from=builder /workspace/target/*.jar app.jar

# Render asigna automáticamente el puerto en la variable $PORT
EXPOSE 8080

# Parámetros de memoria optimizados para el nivel gratuito de Render (512 MB RAM)
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
