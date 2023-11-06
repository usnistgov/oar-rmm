FROM eclipse-temurin:8-jdk-focal 

RUN mkdir -p /usr/share/man/man1
RUN apt-get update && apt-get install -y netcat-openbsd zip git less \
                                            python3 curl maven gnupg
RUN cd /usr/bin && ln -s python3 python

# Define an argument for MongoDB version which can be passed at build time
ARG MONGO_VERSION=4.4

# Install MongoDB
RUN curl -fsSL https://www.mongodb.org/static/pgp/server-$MONGO_VERSION.asc | apt-key add -
RUN apt-key list
RUN echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu focal/mongodb-org/$MONGO_VERSION multiverse" | tee /etc/apt/sources.list.d/mongodb-org-$MONGO_VERSION.list

# Create the user that build/test operations should run as.  Normally,
# this is set to match identity information of the host user that is
# launching the container.
#
RUN sed --in-place -e '/CREATE_MAIL_SPOOL/ s/=yes/=no/' /etc/default/useradd
ARG devuser=developer
ARG devuid=1000
RUN grep -qs :${devuid}: /etc/group || \
    groupadd --gid $devuid $devuser
RUN grep -qs ":${devuid}:[[:digit:]]+:" /etc/passwd || \
    useradd -m --comment "OAR Developer" --shell /bin/bash \
            --gid $devuid --uid $devuid $devuser
RUN mkdir /home/$devuser/.m2

VOLUME /app/dev
VOLUME /app/dist
COPY settings.xml /app/mvn-user-settings.xml
COPY settings.xml /home/$devuser/.m2/settings.xml
RUN chown $devuser:$devuser /home/$devuser/.m2/settings.xml && \
    chmod a+r /home/$devuser/.m2/settings.xml
COPY entrypoint.sh /app/entrypoint.sh
RUN chmod a+rx /app/entrypoint.sh

ENV S3MOCK_JAVA_OPTS -XX:+UseContainerSupport 
WORKDIR /app/dev
USER $devuser

ENTRYPOINT [ "/app/entrypoint.sh" ]


