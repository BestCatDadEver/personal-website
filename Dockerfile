# syntax=docker/dockerfile:1

# ---- Etapa de compilacion ----
# Se usa el wrapper del repo y no una imagen de Gradle preinstalada: asi la
# distribucion se verifica contra distributionSha256Sum tambien dentro del build.
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Las dependencias cambian mucho menos que el codigo: copiarlas primero permite
# que Docker reutilice la capa cuando solo se toca src/.
COPY gradle/ gradle/
COPY gradlew settings.gradle.kts build.gradle.kts gradle.properties ./
RUN chmod +x gradlew && ./gradlew --no-daemon dependencies --quiet || true

COPY src/ src/
RUN ./gradlew --no-daemon buildFatJar \
    && mv build/libs/*-all.jar /app/app.jar

# ---- Etapa de ejecucion ----
# JRE en vez de JDK: la imagen final no lleva compilador ni herramientas de build.
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# El proceso no corre como root: si alguien logra ejecucion dentro del
# contenedor, no arranca con privilegios administrativos.
RUN addgroup -S app && adduser -S -G app app

COPY --from=build --chown=app:app /app/app.jar app.jar

USER app
EXPOSE 8080

# MaxRAMPercentage deja que la JVM respete el limite de memoria del contenedor
# en lugar de asumir la RAM del host.
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-XX:+UseSerialGC", "-jar", "app.jar"]
