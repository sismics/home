#!/bin/bash
docker rm -f sismics_home
docker run \
    -d --name=sismics_home --restart=always \
    -v sismics_home_data:/data \
    -e 'VIRTUAL_HOST_SECURE=myhome.bgamard.org' -e 'VIRTUAL_PORT=80' \
    sismics/home:latest
