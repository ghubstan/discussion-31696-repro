#!/bin/bash
echo "Set up local docker registry with docker host port 5100 and mapped container port 5000"
docker container stop registry
docker container rm -v registry
docker run -dit -p 5100:5000 --name registry registry
echo "To tail the registry log:  $ docker logs -f registry"
