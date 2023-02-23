FROM eclipse-temurin:17-jdk-jammy
#Listen port 8088
EXPOSE 8088
#Copy the app files from host to image the file system
COPY /build /build
#Set the directory for executing commands
WORKDIR /src/main/java/reward/RewardApp
CMD java -jar ./reward-0.0.1-SNAPSHOT.jar
