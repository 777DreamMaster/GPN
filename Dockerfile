# ----
FROM maven:3.6.3-openjdk-17-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests
# ----

#docker run -p 8080:8080 --rm -it gpn2:latest
# Package stage
# ----
FROM openjdk:17-slim
COPY --from=build /home/app/target/GPN-0.1.0-SNAPSHOT.jar /usr/local/lib/gpn.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/gpn.jar"]
# ----