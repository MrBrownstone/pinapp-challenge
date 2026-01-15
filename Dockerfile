FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy only what is needed to build both projects
COPY JNotifier/pom.xml JNotifier/pom.xml
COPY JNotifier/src JNotifier/src
COPY jnotifier-client/pom.xml jnotifier-client/pom.xml
COPY jnotifier-client/src jnotifier-client/src

# Build and install the library, then package the client with deps
RUN mvn -q -f JNotifier/pom.xml -DskipTests install
RUN mvn -q -f jnotifier-client/pom.xml -DskipTests package \
    dependency:copy-dependencies -DincludeScope=runtime \
    -DoutputDirectory=/app/jnotifier-client/target/dependency

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/jnotifier-client/target/jnotifier-client-1.0-SNAPSHOT.jar /app/jnotifier-client.jar
COPY --from=build /app/jnotifier-client/target/dependency /app/dependency

CMD ["java", "-cp", "/app/jnotifier-client.jar:/app/dependency/*", "com.pinapp.App"]
