#Use Gradle 7.6 and java version 16
FROM gradle:7.6-jdk16 AS build
#Listen port 8088
EXPOSE 8088
#Copy the app files from host to image the file system
COPY build/ build
#Set the directory for executing commands
WORKDIR /src/main/java/reward/RewardApp
#Run the main class
CMD java Main