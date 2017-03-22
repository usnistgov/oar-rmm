#!/bin/bash
cd /home/ubuntu/oar-docker/rmm
sudo docker rm -f $(sudo docker ps -a | grep "rmm")
sudo docker rmi -f $(sudo docker images -a | grep "rmm")
sudo docker-compose up -d --build
