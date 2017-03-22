#!/bin/bash
sudo rm -r /opt/data/backup/oar-rmm-service/
if [ -f /home/ubuntu/oar-docker/oar-rmm-service/oar-rmm-service.jar ];
then
  #backup previous build
  sudo cp -r /home/ubuntu/oar-docker/oar-rmm-service.jar /opt/data/backup/oar-rmm-service/
  #remove previous build
  sudo rm -r /home/ubuntu/oar-docker/oar-rmm-service/oar-rmm-service.jar
fi
