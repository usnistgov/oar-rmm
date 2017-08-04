#!/bin/bash
if [ -f /home/ubuntu/oar-docker/apps/rmm/restapi/app/oar-rmm-service.jar ];
then
  #remove previous build
  sudo rm -r /home/ubuntu/oar-docker/apps/rmm/restapi/app/oar-rmm-service.jar
fi
