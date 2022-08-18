FROM openjdk:8
EXPOSE 8082
ADD target/tweetapp.jar tweetapp.jar
ENTRYPOINT ["java", "-jar", "/tweetapp.jar"]