### Build Image ###
FROM amazoncorretto:21-alpine AS BUILD_IMAGE

# Build project
COPY . /spring
WORKDIR /spring
RUN ./gradlew bootJar

# Creating list of dependencies
WORKDIR /spring/build/libs
RUN unzip template-spring-1.0.0.jar
RUN jdeps --print-module-deps --ignore-missing-deps --recursive --multi-release 21 \
    --class-path="BOOT-INF/lib/*" --module-path="BOOT-INF/lib/*" template-spring-1.0.0.jar > /deps.txt

# Installing dependencies
# It must be placed at this point only, otherwise it will fail the Gradle build if put before
# but it's also required so the jlink command works properly.
RUN apk add --no-cache binutils

# Build small JRE image
RUN jlink --verbose \
         --add-modules $(cat /deps.txt) \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /customjre

### Main Image ###
FROM alpine
LABEL maintainer="Vinicius Egidio <me@vinicius.io>"

# Create custom JRE
ENV JAVA_HOME=/jre
ENV PATH="${JAVA_HOME}/bin:${PATH}"
COPY --from=BUILD_IMAGE /customjre $JAVA_HOME

# Define the image version
ARG VERSION
ENV IMAGE_VERSION=$VERSION

COPY --from=BUILD_IMAGE /spring/build/libs/template-spring-1.0.0.jar /var/spring.jar
EXPOSE 3000

CMD ["java", "-jar", "/var/spring.jar"]