#!/bin/bash
echo "Stop and delete local docker registry with docker host port 5100 and mapped container port 5000"
docker container stop registry
docker container rm -v registry
