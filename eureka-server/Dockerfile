FROM openjdk:17-jdk
WORKDIR /app
COPY target/eureka-server-*.jar /app/eureka-server.jar
ENTRYPOINT ["java", "-jar", "/app/eureka-server.jar"]
