#!/usr/bin/env bash

set -e
set -v

docker build -t healthweb/healthweb:h2 .
# docker push healthweb/healthweb:h2
