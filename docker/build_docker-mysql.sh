#!/usr/bin/env bash

set -e
set -v

docker build -f docker/Dockerfile-mysql -t healthweb-h2 .
