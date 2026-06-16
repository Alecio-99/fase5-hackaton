# ============================================================
# Estágio 1 — Build (compila e empacota com o Maven Wrapper)
# ============================================================
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Copia primeiro os arquivos de build para aproveitar o cache de camadas
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Baixa as dependências (camada cacheada enquanto o pom.xml não mudar)
RUN chmod +x mvnw && ./mvnw -q -B dependency:go-offline

# Copia o código-fonte e empacota o JAR (testes rodam no pipeline/deploy.sh)
COPY src src
RUN ./mvnw -q -B clean package -DskipTests

# ============================================================
# Estágio 2 — Runtime (imagem enxuta apenas com o JRE)
# ============================================================
FROM eclipse-temurin:17-jre AS runtime
WORKDIR /app

# Usuário não-root por segurança
RUN useradd -r -u 1001 spring
USER spring

COPY --from=build /app/target/hackton-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
