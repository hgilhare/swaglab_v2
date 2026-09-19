FROM maven:3.9.16-eclipse-temurin-21

WORKDIR /app

ENV TZ=Asia/Kolkata


RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        nginx \
        wget \
        tar \
    && rm -rf /var/lib/apt/lists/*

RUN wget -O /tmp/allure.tgz \
        https://github.com/allure-framework/allure2/releases/download/2.45.0/allure-2.45.0.tgz \
    && tar -xzf /tmp/allure.tgz -C /opt/ \
    && ln -s /opt/allure-2.45.0/bin/allure /usr/local/bin/allure \
    && rm /tmp/allure.tgz

RUN allure --version && \
    java -version && \
    mvn -v && \
    nginx -v