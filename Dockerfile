FROM cgr.dev/chainguard/jdk:latest
WORKDIR /home/nonroot/app
COPY target/cozinha-0.0.1-SNAPSHOT.jar /home/nonroot/app/app.jar
EXPOSE 9090
CMD ["java", \
     "-XX:+UseContainerSupport", \
     "-XX:MaxRAMPercentage=75.0", \
     "-XX:InitialRAMPercentage=50.0", \
     "-jar", "app.jar"]
