FROM eclipse-temurin:23-jdk-alpine as build
WORKDIR /workspace/app

# Copiar el archivo pom.xml y los archivos del proyecto
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Dar permisos de ejecución al script mvnw
RUN chmod +x ./mvnw

# Construir el JAR
RUN ./mvnw package -DskipTests

# Crear la imagen final más pequeña
FROM eclipse-temurin:23-jre-alpine
VOLUME /tmp
COPY --from=build /workspace/app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]