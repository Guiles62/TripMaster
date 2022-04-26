FROM java:8
VOLUME /tmp
COPY ./build/libs/gpsUtil.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]

