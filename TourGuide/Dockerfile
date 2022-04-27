FROM java:8
VOLUME /tmp
COPY ./build/libs/tourGuide.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]