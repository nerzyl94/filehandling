# File Handling in Java

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Instruction](#instruction)
* [Launch](#launch)

## General info
Application to work with csv files.

My task is to read all data from the input csv file, create a report and write it to a new file. 

## Technologies:
- Spring Boot, Spring Data 2.6.7
- Java version 17
- Postgresql
- Maven
- Lombok

## Instruction
This application executes a method every 5000 milliseconds that scans a folder (the default folder is C:\products\),
processes the files and generates reports.

You may change default folder in application.yml

Before running the application, you need to have a postgres database on your computer and create a database. 
You should change the connection data to your own in the file application.yml.

## Launch


You can build a project using maven with command
```
mvn clean install
```
Then you can run application with command
```
java -jar file-handling-0.0.1-SNAPSHOT.jar
```

Or you can just run it into your IDE.


