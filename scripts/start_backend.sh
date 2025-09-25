#!/usr/bin/env bash

CURRENT_DIR="$(cd "$(dirname "$0")" && pwd)"
BASE_DIR="$CURRENT_DIR/.."
BACKEND_DIR= "$BASE_DIR/SmartServiceHub/SmartServiceHub-backend"
LOG_DIR="$BASE_DIR/logs"

cd "$BACKEND_DIR" || exit
mvn spring-boot:run 2>&1 | tee $LOG_DIR/backend.log
