#!/usr/bin/env bash

source ./config.sh

cd "$BACKEND_DIR" || exit
mvn spring-boot:run 2>&1 | tee $BACKEND_LOG
