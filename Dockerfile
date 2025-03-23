FROM openjdk:23-jdk

WORKDIR /app

COPY DeliveryApplication.jar /app/DeliveryApplication.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/DeliveryApplication.jar"]