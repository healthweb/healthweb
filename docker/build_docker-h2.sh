#!/usr/bin/env bash

set -e
set -v

docker build -f docker/Dockerfile-h2 -t healthweb/healthweb:h2 .
docker push healthweb/healthweb:h2
