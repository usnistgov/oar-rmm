#!/bin/bash
cd /home/ubuntu/oar-docker/rmm

if [[ $(sudo docker ps -a | grep "rmm") ]]; then
    sudo docker rm -f $(sudo docker ps -a | grep "rmm")
fi
if [[ $(sudo docker images -a | grep "rmm") ]]; then
   sudo docker rmi -f $(sudo docker images -a | grep "rmm")
fi

sudo docker-compose rm -f
sudo docker-compose build --no-cache
sudo docker-compose up -d 
