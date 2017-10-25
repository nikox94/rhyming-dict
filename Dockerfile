FROM java:8
LABEL Description="Rhyming dict app" Version="0.0.1"
MAINTAINER serene.blog.7@gmail.com

VOLUME /tmp

EXPOSE 8080

ENV USER_NAME dict
ENV APP_HOME /home/$USER_NAME/app
RUN useradd -ms /bin/bash $USER_NAME
RUN mkdir $APP_HOME

ADD target/rhyming-dict-0.0.1-SNAPSHOT.jar $APP_HOME/dict.jar
RUN chown $USER_NAME $APP_HOME/dict.jar

USER $USER_NAME
WORKDIR $APP_HOME

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","dict.jar"]
