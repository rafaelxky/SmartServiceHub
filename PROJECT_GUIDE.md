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

## Scripting
The lua scripts use an observer model to subsctribe to program events

## DB
"users" hold basic user data 
"services" have a reference its creator
"comments" have a reference to the creator and the "service" they bellong to

erDiagram
    USERS {
        int id
        string name
        string email
    }
    SERVICES {
        int id
        string title
        string content
        int creator
    }
    COMMENTS {
        int id
        string content
        int creator
        int service
    }

    USERS ||--o{ SERVICES : "creator"
    USERS ||--o{ COMMENTS : "creator"
    SERVICES ||--o{ COMMENTS : "service"



