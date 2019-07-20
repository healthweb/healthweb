#!/usr/bin/env bash

set -e
set -v

mkdir docker
cd docker
mvn dependency:get -B -s .travis/maven_settings.xml \
  -DrepoUrl=http://repo.maven.apache.org/maven2/ \
  -Dartifact=$1 \
  -Dtransitive=true \
  -Ddest=db-driver.jar
docker build
