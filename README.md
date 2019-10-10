# Spring ThymeLeaf (boostrap) and REST API
This is a sample of how one can expose a REST API to external users and also provide a light weight admin section
using ThymeLeaf and boostrap

## Prerequisite
In order to install and start the application please ensure that you have the following:
- Java JDK (v8+)
- Maven (v3+)
- MySQL (optional MySQL Workbench)

## frameworks used
- Spring  boot
- Spring security 
- JWT
- ThymeLeaf and bootstrap


## Instructions

1. The code can be found in github repo [communications](https://github.com/garytxo/communications)

2. Clone repository locally using

   `git clone https://github.com/garytxo/communications`
      
3. Install dependencies and run test
    
    `mvn clean install`

4. Start your server using 
    
    `mvn spring-boot:run`
    
5. To view the REST API endpoints check the swagger documentation [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

6. Also you can  [login](http://localhost:8080/login) into the admin section using `admin@admin.com` : `admin`


## Change H2 to Mysql database
By default the application is configured to run against H2 database, in order to change to run against mysql please following the instructions
below:

1. create the financial database schema by opening a terminal and executing the following command
       
       `mysql -uroot -Bse'CREATE DATABASE communications'`
       
2. Update the application-mysql.properties connection details

3. Change the active profile to ``mysql` in the application.properties 
    
    `spring.profiles.active=mysql`
    

## using REST API
There is a two step policy to use the REST API:
1. One needs to get a authorization token by calling : `curl -X POST "http://localhost:8080/auth/signin" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"password\": \"admin\", \"username\": \"admin@admin.com\"}"`

2. Then using the token in the authoriation header when can perform REST calls such as a new message:
`curl -X POST "http://localhost:8080/v1/sms" -H "accept: */*" -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBhZG1pbi5jb20iLCJyb2xlcyI6WyJST0xFX1VTRVIiLCJST0xFX0FETUlOIl0sInVzZXJJZCI6MSwiYmFuZHdpZHRoSWQiOjEsImlhdCI6MTU3MDcyMDY5MiwiZXhwIjoxNTcwNzIwODcyfQ.nu4M6ghYD2ohx9KWGhfoD-8CEEzRKXeuNsTPABU5QdY" -H "Content-Type: application/json" -d "{ \"message\": \"Hello world\", \"receiver\": 4175409749, \"sendOn\": \"2019-10-13\", \"sender\": 18445014846}"`


## For further info check 
* [SpringBoot and JWT](https://github.com/hantsy/springboot-jwt-sample)
* [Use ThymeLeaf for frontend](https://dimitr.im/consuming-rest-apis-with-spring)
* [Bandwidth Application](https://dev.bandwidth.com/account/applications/about.html)
* [SMS Callbacks](https://dev.bandwidth.com/messaging/callbacks/messageEvents.html)
* [Bootstrap v3](https://getbootstrap.com/docs/3.3)
* [Date selector](https://tempusdominus.github.io/bootstrap-3)
