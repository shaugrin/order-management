FROM openjdk:17-jdk
WORKDIR /app
COPY target/order-service-*.jar /app/order-service.jar
ENTRYPOINT ["java", "-jar", "/app/order-service.jar"]
