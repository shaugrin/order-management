FROM openjdk:17-jdk
WORKDIR /app
COPY target/notification-*.jar /app/notification.jar
ENTRYPOINT ["java", "-jar", "/app/notification.jar"]
