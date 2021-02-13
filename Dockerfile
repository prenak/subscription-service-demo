FROM openjdk:11
ADD target/subscription-service-demo.jar subscription-service-demo.jar
EXPOSE 8089
ENTRYPOINT ["java","-jar","subscription-service-demo.jar","--spring.profiles.active=qa"]