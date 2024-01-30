FROM sbtscala/scala-sbt:eclipse-temurin-focal-11.0.21_9_1.9.8_2.13.12 AS BUILD_IMAGE
ENV APP_HOME=/root/dev/play-scala-seed/
#RUN mkdir -p $APP_HOME/src/main/scala
WORKDIR $APP_HOME
#COPY build.gradle gradlew gradlew.bat $APP_HOME
COPY ./play-scala-seed ./
# download dependencies
#RUN sbt update
RUN sbt dist
RUN sbt playUpdateSecret

FROM openjdk:11-jre
WORKDIR /root/
COPY --from=BUILD_IMAGE /root/dev/play-scala-seed/target/universal/play-scala-seed-1.0-SNAPSHOT.zip .
RUN unzip play-scala-seed-1.0-SNAPSHOT.zip
#CMD ["play-scala-seed-1.0-SNAPSHOT/bin/play-scala-seed" "-Dconfig.file", "/full/path/to/conf/application-prod.conf"]
CMD ["./play-scala-seed-1.0-SNAPSHOT/bin/play-scala-seed", "-Dplay.http.secret.key='QCY?tAnfk?aZ?iwrNwnxIlR6CTf:G3gf:90Latabg@5241AB`R5W:1uDFN];Ik@n'"]