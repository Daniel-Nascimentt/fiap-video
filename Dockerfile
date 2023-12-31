FROM openjdk:17

WORKDIR /app

COPY target/*.jar /app/fiap-video.jar

#porta padrao do mongodb
ENV MONGO_HOST mongodb://meu-mongo:27017/fiap-video

EXPOSE 8080

CMD ["java", "-jar", "fiap-video.jar"]
