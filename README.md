## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)

## General info
An application which regularly scrapes currency data from internet and serve it with an API.

## Technologies
Project is created with:
* Python:3.11
* Selenium:4.10.0
* Springboot:3.0.2
* Keycloack:21.0
* ELK:8.3.3
* Postgres:latest

## Setup
To run this project,download the CurrencyApp file and write console docker-compose up then application will start.When the application start you must do some configuration in app.

### Keycloack
Firstly, go to http://172.24.2.5:8080 in your browser and enter username: admin password: admin then the keycloack will start.Second step is creating a realm named currency.After that create a client named currency-api in this client create role named currency_admin , currency_banker and currency_user.These roles will be our customers' role.Following that associate all of the roles with admin, banker and user which are created in realm roles.Lastly create user in users part and add role to this user.(In the documentation file users are aliak, banker1, user1 with passwords with same their names.If you want , you can change their names and password but be careful you must change body part of request when generating token.) And thats it keycloack part is done.

Additionally, I upload a JSON file named realm-export.json which contains currency realm to use.If you dont want to deal with it ,simply import this file.You have to do only create users and assign roles to these users.

### Spring
https://documenter.getpostman.com/view/22875373/2s9Xy3rWSt there are all endpoints and requests in spring part.The important point is when you want to access the request , you have to generate token which role do you want in generate tokens folder.Then copy and paste this token to authorization-bearer token part.

### Kibana
You can reach all application logs with http://0.0.0.0:5601 url . The thing we do in this part creating data view which is named *spring.You can see the logs in discover part and if you want you can also create dashboards.

### Python-Selenium
As default application scrapes data per 1 minute from internet.You can change this interval with navigate to pythonApp/crontab file and fix it.

### Postgres
I uploaded a screenshot shows how data stored in database.





