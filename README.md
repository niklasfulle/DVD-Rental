# DVD-Rental

## Table of contents

-   [Overview](#overview)
-   [External dependencies](#external-dependencies)
-   [Project structure](#project-structure)
-   [REST API Endpoints](#rest-api-endpoints)
-   [Build](#build)
-   [Features](#features)
-   [Screenshots](#screenshots)

## Overview

This project aims to implement a dvd rental website. It's building .war files, deployable on a wildfly webserver.
It is designed to be a microservice architecture,<br> with a frontend, a backend and a PostgreSQL database.
With three REST APIs, the frontend can access the backend, which can access the database.<br> This was a project for university of my software engineering course.
It represents the REST API acces points who are given in the .openapi files in the docs folder.<br>

## External dependencies

| Name        | Version | Link                                                                |
| ----------- | ------- | ------------------------------------------------------------------- |
| Java JDK    | 19      | https://jdk.java.net/19/                                            |
| Wildfly     | 28      | https://www.wildfly.org/                                            |
| PostgreSQL  | 15.3    | https://www.postgresql.org/                                         |
| JBDC Driver | 42.6.0  | https://mvnrepository.com/artifact/org.postgresql/postgresql/42.6.0 |

## Project structure

-   dvd-rental-customer
-   dvd-rental-film
-   dvd-rental-store
-   dvd-rental-ui

## REST API Endpoints

| Name     | Endpoint                                            |
| -------- | --------------------------------------------------- |
| Customer | http://localhost:8080/dvd-rental-customer/resources |
| Film     | http://localhost:8080/dvd-rental-film/resources     |
| Store    | http://localhost:8080/dvd-rental-store/resources    |

## Build

To build this project, clone this repository and install the external dependencies.<br>
First import the three `.sql` files into your PostgreSQL database. The files is located in `docs`.<br>

You have to create a wildfly user with the add-user.bat or add-user.sh script in the bin folder of your wildfly installation.<br>

To build this project with maven, run the following command in the root directory of every `dvd-rental-*` project:<br>

```
mvn clean package
```

The JBDC Driver has to be into `standalone/deployments` in your wildfly installation.
This creates a `dvd-rental-*.war` files,<br> which has to be copied to your wildfly 28
installation into `standalone/deployments` in your wildfly installation.<br>
Start the wildfly server with `standalone.bat` for windows or `standalone.sh` for linux in `bin/`.

## Features

## Screenshots
