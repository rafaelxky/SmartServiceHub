
#!/bin/bash

# Force script to run as root
if [ "$EUID" -ne 0 ]; then
  echo "This script must be run with sudo. Re-running with sudo..."
  exec sudo "$0" "$@"
fi

# Colors
YELLOW='\033[1;33m'
NC='\033[0m'

# Absolute path to nginx.conf
NGINX_CONF="$(cd "$(dirname "$0")" && pwd)/nginx.conf"

echo -e "${YELLOW}Starting NGINX...${NC}"

# Start NGINX in background and log output
nginx -c "$NGINX_CONF" 2>&1 | tee nginx.log 

