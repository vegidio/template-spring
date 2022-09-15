### Build Image ###
FROM alpine AS BUILD_IMAGE

# Installing OpenJDK 17
RUN apk add --no-cache openjdk17 binutils --repository=https://dl-cdn.alpinelinux.org/alpine/edge/community

# Build project
COPY . /spring
WORKDIR /spring
RUN ./gradlew bootJar

# Creating list of dependencies
WORKDIR /spring/build/libs
RUN unzip template-spring-1.0.0.jar
RUN jdeps --print-module-deps --ignore-missing-deps --recursive --multi-release 17 \
    --class-path="BOOT-INF/lib/*" --module-path="BOOT-INF/lib/*" template-spring-1.0.0.jar > /deps.txt

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