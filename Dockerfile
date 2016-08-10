FROM java:8u92-jre-alpine
RUN mkdir -p /elastictransfor
COPY ./build/libs/elastictransfor-1.0.jar /elastictransfor/
WORKDIR /elastictransfor
ENTRYPOINT ["java","-jar", "/elastictransfor/elastictransfor-1.0.jar"]
