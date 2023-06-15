FROM c
WORKDIR /app
COPY ./build/libs/front-0.0.1-SNAPSHOT.jar /app/miApp.jar
ENTRYPOINT ["java", "-jar", "miApp.jar"]