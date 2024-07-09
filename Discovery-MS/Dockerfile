FROM openjdk:21-jdk
EXPOSE 8761
ADD /target/Discovery-MS-0.0.1-SNAPSHOT.jar eureka-server-0.0.1.jar
ENTRYPOINT ["java","-jar","eureka-server-0.0.1.jar"]
