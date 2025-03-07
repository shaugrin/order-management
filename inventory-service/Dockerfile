FROM openjdk:17-jdk
WORKDIR /app
COPY target/inventory-service-*.jar /app/inventory-service.jar
ENTRYPOINT ["java", "-jar", "/app/inventory-service.jar"]
