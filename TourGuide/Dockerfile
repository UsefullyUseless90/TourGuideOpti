#Use Gradle 7.6 and java version 16
FROM gradle:7.1-jdk11 AS build
#Listen port 8080
EXPOSE 8080
#Copy the app files from host to image the file system
COPY build/ build
#Set the directory for executing commands
WORKDIR /src/main/java/ /tourGuide
#Run the main class
CMD java Main