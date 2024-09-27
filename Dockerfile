FROM eclipse-temurin:17-jdk-alpine
EXPOSE 8080
ADD target/d2csgame.jar d2csgame.jar
ENTRYPOINT ["java","-jar","d2csgame.jar"]