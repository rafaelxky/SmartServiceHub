
This document facilitates project navigation and contributing

# Structure
It is divided firstly into 2 parts, the backend and frontend

# Conventions
## Backend
The backend follows Spring and MVC standards
The naming convention follows Java's standard conventions

## Frontend
Frontend follows the Vite standards
Naming conventions are Typescript / JavaScript standards

## DB
The DB naming follows snake case (service_hub)
The table names are all lowercase and plural (users, comments, services)

## Documentation
Sentences in documentation should start with an uppercase letter and end with "!", "?", "..." or nothing at all depending on the situation but shouldn't end with "." altough exceptions may apear
Titles use Pascal Case

# Technology
## Languages
The project official languages are as follows
- Java (backend)
- Kotlin (backend)
- Lua (backend scripting)
- Javascript (frontend)
- Typescript (frontend)
- Tsx (frontend)
- Html (frontend)
- Css (frontend)
- Bash Linux (scripting)

## Programs
- Maven (java dependency manager)
- Spring (java framework)
- Npm (js dependency manager)
- Vue (js framework)
- Preact (js framework, lightweight alternative to react but almost the same)
- Postgres (DB)

# Architecture
## Connection
The frontend and backend communicate through REST api 
It uses the http protocol altough https and http2 support should be implemented soon™

## Lua Scripting
The lua scripts use an observer model to subscribe to program events
They work for the backend and can be found under SmartServiceHub/scripts
Any .lua files under this directory will be valid scripts and will be loaded by the program

## Shell Scripts
Shell scripts can be found under ./scripts except for quick setup scripts such as start.sh and stop.sh

## Tor Setup
All tor related scripts and notes should be under ./.tor

## DB
"users" hold basic user data 
"services" have a reference its creator
"comments" have a reference to the creator and the "service" they belong to


