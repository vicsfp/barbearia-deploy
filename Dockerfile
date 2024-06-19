# Etapa de build
FROM maven:3.8.4-openjdk-17-slim AS build

# Copia todos os arquivos do projeto para o contêiner
COPY . .

# Executa o comando de build do Maven
RUN mvn clean install -DskipTests=true

# Verifica o conteúdo do diretório target após o build
RUN ls -l /target

# Etapa final
FROM openjdk:17-slim

EXPOSE 8080

# Copia o arquivo JAR do estágio de build para o contêiner final
COPY --from=build /target/barbearia-gs-1.0.0.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
