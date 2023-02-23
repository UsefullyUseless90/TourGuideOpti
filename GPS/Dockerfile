#Use Gradle 7.1 and java version 11
FROM eclipse-temurin:17-jdk-jammy
#Listen port 8090
EXPOSE 8090
#Copy the app files from host to image the file system
COPY /build /build
#Set the directory for executing commands
WORKDIR /src/main/java/gps/GpsApp
CMD java -jar ./gps-0.0.1-SNAPSHOT.jar
