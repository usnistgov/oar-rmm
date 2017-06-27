#!/bin/bash
cd /home/ubuntu/oar-docker/apps/

if [[ $(sudo docker ps -aqf "name=rmm") ]]; then
    sudo docker rm -f $(sudo docker ps -aqf "name=rmm")
fi
if [[ $(sudo docker images rmm -aq) ]]; then
   sudo docker rmi -f $(sudo docker images rmm -aq)
fi

sudo docker-compose rm -f
sudo docker-compose build --no-cache
sudo docker-compose -f docker-compose.yml -f docker-compose.dev.yml up -d
