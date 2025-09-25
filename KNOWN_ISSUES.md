This is a list of known issues and how to solve them:

# Nginx doesn't restart if you run start_nginx.sh twice
This means an instance of nginx is already running
Kill the previous instance of nginx and try again
The nginx.log file should say something like "port already in use" if this is the issue
To avoid this, use the restart script instead or kill the previous instance before creating a new one

# Frontend or backend isn't working
Check if the ports are in use with the **checker.sh** under **/scripts**
If that is the issue you need to kill the program that is using those ports
Otherwise check the logs

# Shell scripts don't work on windows
Some if not all scripts won't work on non unix based systems
The program and scripts were made on a linux machine wich may cause issues for non linux users
This will be fixed in the future with the integration of docker



