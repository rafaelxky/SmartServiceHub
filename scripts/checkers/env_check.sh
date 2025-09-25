#!/usr/bin/env bash

echo "======== Environment Check ========"

check_command() {
  local cmd=$1
  local name=$2
  if ! command -v $cmd &> /dev/null; then
    echo "❌ $name is NOT installed"
    return 1
  else
    echo "✅ $name is installed: $($cmd --version | head -n1)"
  fi
}

check_command java "Java"
check_command mvn "Maven"
check_command node "Node.js"
check_command npm "NPM"
check_command psql "Postgres"
check_command nginx "Nginx"

echo "==== Environment Check Complete ===="
