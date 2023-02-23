FROM eclipse-temurin:17-jdk-jammy
#Listen port 8080
EXPOSE 8080
#Copy the app files from host to image the file system
COPY /build /build
#Set the directory for executing commands
WORKDIR /src/main/java/ /tourGuide
CMD java -jar ./tourGuide-0.0.1-SNAPSHOT.jar
