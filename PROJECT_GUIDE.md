
# Project Guide

This document facilitates project navigation and contributing

## Structure
The project is divided into two main parts: the **Backend** and **Frontend**

## Conventions

### Backend
- Follows Spring and MVC standards  
- Naming conventions follow Java standard conventions

### Frontend
- Follows Vite standards  
- Naming conventions follow JavaScript/TypeScript standards

### DB
- Uses snake_case for table and column names  
- Table names are lowercase and plural: `users`, `services`, `comments`  
- Relationships:  
  - `users` hold basic user data  
  - `services` reference their creator (`users`)  
  - `comments` reference both the creator (`users`) and the service they belong to (`services`)

### Documentation
- Sentences start with uppercase letters  
- Sentences may end with `!`, `?`, `...`, or nothing, but should generally **not** end with `.`  
- Titles use [title case](https://en.wikipedia.org/wiki/Title_case) 

## Technology

### Languages
- Java, Kotlin (backend)  
- Lua (backend scripting)  
- JavaScript, TypeScript, TSX, HTML, CSS (frontend)  
- Bash (scripting)

### Programs
- Maven (Java dependency manager)  
- Spring (Java framework)  
- Npm (JS dependency manager)  
- Preact (JS framework, lightweight React alternative)  
- PostgreSQL (Database)
