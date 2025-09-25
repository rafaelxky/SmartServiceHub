This is a list of known issues and how to solve them:

# Nginx doesn't restart if you run start_nginx.sh twice
This means an instance of nginx is already running
Kill the previous instance of nginx and try again
The nginx.log file should say something like "port already in use" if this is the issue
To avoid this, use the restart script instead or kill the previous instance before creating a new one




