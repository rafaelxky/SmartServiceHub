#!/usr/bin/env bash

echo "==== Port Check ===="
PORTS=(8080 8081 8082)
for PORT in "${PORTS[@]}"; do
  if lsof -i:$PORT &> /dev/null; then
    echo "❌ Port $PORT is already in use"
  else
    echo "✅ Port $PORT is free"
  fi
done
echo "==================="
