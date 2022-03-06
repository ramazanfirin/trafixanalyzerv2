FROM openjdk:8-jre-alpine
#COPY --from=python:3.6 / /

#RUN apk add --update ffmpeg

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JHIPSTER_SLEEP=0 \
    JAVA_OPTS=""

CMD echo "The application will start in ${JHIPSTER_SLEEP}s..." && \
    sleep ${JHIPSTER_SLEEP} && \
    java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app.war

EXPOSE 8080

ADD *.war /app.war
#ADD velvet-video-natives-0.2.8.full.jar /velvet-video-natives-0.2.8.full.jar
