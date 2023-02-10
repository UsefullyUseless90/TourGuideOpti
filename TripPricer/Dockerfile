#Use Gradle 7.6 and java version 16
FROM gradle:7.6-jdk16 AS build
#Listen port 8092
EXPOSE 8092
#Copy the app files from host to image the file system
COPY build/ build
#Set the directory for executing commands
WORKDIR /src/main/java/trippricer/App
#Run the main class
CMD java Main