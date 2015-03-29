FROM gfx2015/android:latest

MAINTAINER FUJI Goro <g.psy.va@gmail.com>

ENV PROJECT /project

RUN mkdir $PROJECT
WORKDIR $PROJECT

ADD . $PROJECT

RUN echo "sdk.dir=$ANDROID_HOME" > local.properties
RUN ./gradlew --stacktrace androidDependencies

CMD ./gradlew --stacktrace test build
