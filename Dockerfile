FROM openjdk:8-jdk-alpine

MAINTAINER egormaksimchuk@gmail.com

COPY ./target/kubernetes-client-demo-1.0.1.jar /

EXPOSE 8081

ENTRYPOINT ["java","-jar","kubernetes-client-demo-1.0.1.jar"]