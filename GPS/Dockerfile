#Use Gradle 7.6 and java version 16
FROM gradle:7.6-jdk16 AS build
#Copy the app files from host to image the file system
COPY gps/src/main/java/ /GpsApp
#Set the directory for executing commands
WORKDIR /GpsApp
#Run the main class
CMD java Main