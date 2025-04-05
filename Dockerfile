# Use an OpenJDK base image
FROM openjdk:17-jdk-slim
# Set the working directory in the container
WORKDIR /app
# Copy the JAR file into the container
COPY /out/artifacts/Java_Snake_Game_jar/Java-Snake-Game.jar /app/app.jar
# Command to run the JAR file
CMD ["java", "-jar", "/app/app.jar"]