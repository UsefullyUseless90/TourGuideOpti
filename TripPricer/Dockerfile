FROM eclipse-temurin:17-jdk-jammy
#Listen port 8092
EXPOSE 8092
#Copy the app files from host to image the file system
COPY /build /build
#Set the directory for executing commands
WORKDIR /src/main/java/trippricer/App
CMD java -jar ./trippricer-0.0.1-SNAPSHOT.jar
