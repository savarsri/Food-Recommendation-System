FROM openjdk:21-jdk
EXPOSE 8085
LABEL authors="savar"
ADD /target/Authentication-Server-0.0.1-SNAPSHOT.jar authentication-server-0.0.1.jar
ENTRYPOINT ["java","-jar","authentication-server-0.0.1.jar"]