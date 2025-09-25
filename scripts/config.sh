

#--------------------
# BASE DIRECTORIES 
#--------------------
SCRIPTS_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BASE_DIR="$SCRIPTS_DIR/.."
TEMP_DIR="$BASE_DIR/.temp"
LOG_DIR="$BASE_DIR/logs"
PROGRAM_DIR="$BASE_DIR/SmartServiceHub"
SRV_DIR="$BASE_DIR/srv"

BACKEND_DIR="$PROGRAM_DIR/SmartServiceHub-backend"
FRONTEND_DIR="$PROGRAM_DIR/SmartServiceHub-frontend/srv/frontend"

#--------------------
# PID FILES
#--------------------
BACKEND_PID="$TEMP_DIR/backend.pid"
FRONTEND_PID="$TEMP_DIR/frontend-pid"
NGINX_PID="$TEMP_DIR/nginx.pid"

#--------------------
# LOGS
#--------------------
BACKEND_LOG="$LOG_DIR/backend.log"
FRONTEND_LOG="$LOG_DIR/frontend.log"
NGINX_LOG="$LOG_DIR/nginx.log"

NGINX_ERROR_LOG="$LOG_DIR/nginx_error.log"
NGINX_ACCESS_LOG="$LOG_DIR/nginx_access.log"

#--------------------
# NGINX 
#--------------------
NGINX_CONF="$SRV_DIR/nginx.conf"
NGINX_CONF_TEMPLATE="$SRV_DIR/nginx.conf.template"

#--------------------
# URLS
#--------------------
MVN_URL="http://localhost/status"
NGINX_URL="http://localhost:8081"

#--------------------
# COLORS
#--------------------
GREEN="\e[32m"
YELLOW="\e[33m"
RED="\e[31m"
NC="\e[0m"
