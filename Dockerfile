FROM openjdk:21-jdk

LABEL maintainer="Rychu"

COPY target/UserInteractionMicroService-0.0.1-SNAPSHOT.jar UserInteractionMicroService-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "UserInteractionMicroService-0.0.1-SNAPSHOT.jar"]