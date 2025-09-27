#!/usr/bin/env bash

source ./config.sh

echo -e "${YELLOW}Started frontend${NC}"

cd $FRONTEND_DIR || exit
npm run dev > "$FRONTEND_LOG" 2>&1 &
echo $! > "$FRONTEND_PID"

