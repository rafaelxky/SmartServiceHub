#!/usr/bin/env bash

source ./config.sh

if [ -f $BACKEND_PID ]; then
  kill "$(cat $BACKEND_PID)" && echo -e "${GREEN} Stopped backend ${NC}"
  rm $BACKEND_PID
else
  echo -e "${RED} No backend PID file found ${NC}"
fi
