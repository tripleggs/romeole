FROM hub.c.163.com/library/java:8-alpine
ADD / /app/
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
WORKDIR /app
RUN mvn install -Dmaven.test.skip=true
RUN mv ./target/app.jar /
RUN mvn clean
WORKDIR /
RUN mkdir /config
VOLUME /var/log/romeole /var/log/romeole
VOLUME /config
EXPOSE 8580
CMD ["java","-jar","-Duser.timezone=GMT+08","app.jar","--server.port=8580","--spring.profiles.active=dev"]