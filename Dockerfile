FROM openjdk:17

WORKDIR /app

COPY target/booking-0.0.1-SNAPSHOT.jar .

CMD java -jar booking-0.0.1-SNAPSHOT.jar


