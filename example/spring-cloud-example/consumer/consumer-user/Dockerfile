FROM apache/skywalking-java-agent:8.16.0-java17
ENV TZ="Asia/Shanghai"
EXPOSE 81
EXPOSE 8720
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /home/app.jar
ENTRYPOINT ["java","-jar","/home/app.jar"]