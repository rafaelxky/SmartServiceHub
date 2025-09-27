

#--------------------
# BASE DIRECTORIES 
#--------------------
SCRIPTS_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BASE_DIR="$SCRIPTS_DIR/.."
TEMP_DIR="$BASE_DIR/.temp"
LOG_DIR="$BASE_DIR/logs"
PROGRAM_DIR="$BASE_DIR/SmartServiceHub"
SRV_DIR="$BASE_DIR/srv"
CONFIGS="$SCRIPTS_DIR/configs"
REDIS_DIR="$SCRIPTS_DIR/redis"

BACKEND_DIR="$PROGRAM_DIR/SmartServiceHub-backend"
FRONTEND_DIR="$PROGRAM_DIR/SmartServiceHub-frontend/srv/frontend"

#--------------------
# PID FILES
#--------------------
BACKEND_PID="$TEMP_DIR/backend.pid"
FRONTEND_PID="$TEMP_DIR/frontend-pid"
NGINX_PID="$TEMP_DIR/nginx.pid"
REDIS_PID="$TEMP_DIR/redis-server.pid"

#--------------------
# LOGS
#--------------------
BACKEND_LOG="$LOG_DIR/backend.log"
FRONTEND_LOG="$LOG_DIR/frontend.log"
NGINX_LOG="$LOG_DIR/nginx.log"
REDIS_LOG="$LOG_DIR/redis.log"

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
REDIS_URL="http://localhost:6379"

#--------------------
# REDIS
#--------------------
REDIS_CONFIG=$CONFIGS"/redis.conf"
REDIS_CONFIG_TEMPLATE=$CONFIGS"/redis.conf.template"

#--------------------
# COLORS
#--------------------
GREEN="\e[32m"
YELLOW="\e[33m"
RED="\e[31m"
NC="\e[0m"
