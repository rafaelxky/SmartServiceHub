#!/usr/bin/env bash

source ../scripts/config.sh

if [ -f $NGINX_PID ]; then
  kill "$(cat $NGINX_PID)" && echo -e "${GREEN} Stopped nginx ${NC}"
  rm $NGINX_PID
else
  echo -e "${RED} No nginx PID file found ${NC}"
fi
