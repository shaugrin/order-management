FROM openjdk:17-jdk
WORKDIR /app
COPY target/product-service-*.jar /app/product-service.jar
ENTRYPOINT ["java", "-jar", "/app/product-service.jar"]
