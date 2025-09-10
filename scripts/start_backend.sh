#!/bin/bash

BASE_DIR="$(dirname "$0")/../SmartServiceHub/SmartServiceHub-backend"
cd "$BASE_DIR" || exit
mvn spring-boot:run 2>&1 | tee backend.log
