#!/usr/bin/env bash


SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_FILE="$SCRIPT_DIR/config.sh"
source "$CONFIG_FILE"

echo -e "${YELLOW}Started frontend${NC}"

cd $FRONTEND_DIR || exit
npm run dev > "$FRONTEND_LOG" 2>&1 &
echo $! > "$FRONTEND_PID"

printf "${GREEN}Frontend URL: ${NC}http://localhost:8082/\n"
