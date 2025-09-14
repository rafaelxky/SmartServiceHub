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

# Architecture
## Connection
The frontend and backend communicate trough REST api 
It uses the http protocol altough https and http2 support should be implemented soon™

## Lua Scripting
The lua scripts use an observer model to subsctribe to program events

## Shell scripts
Shell scripts can be found under ./scripts except for quick setup scripts such as start.sh and stop.sh

## Tor setup
All tor related scripts and notes should be under ./.tor

## DB
"users" hold basic user data 
"services" have a reference its creator
"comments" have a reference to the creator and the "service" they bellong to

classDiagram
    class User {
        int id
        string name
        string email
    }

    class Service {
        int id
        string title
        string content
        int creator
    }

    class Comment {
        int id
        string content
        int creator
        int service
    }

    User "1" --> "many" Service : creates
    User "1" --> "many" Comment : writes
    Service "1" --> "many" Comment : receives

