## About

[![CI Pipeline](https://github.com/AndrewPleskanko/jira-progect/actions/workflows/maven.yml/badge.svg)](https://github.com/AndrewPleskanko/jira-progect/actions)

This project is a simple RESTful API for managing users. It provides endpoints for creating, updating, deleting, and
retrieving users. This project is an email microservice in Java. It sends email notifications and tracks their delivery status.
Emails are sent reliably, even if the email server is temporarily unavailable.

## System design

In this project, we utilize the [Strategy pattern](https://refactoring.guru/design-patterns/strategy) to generate
reports in various formats. The ReportStrategy interface is
implemented by different classes, each responsible for generating a report in a specific format like CSV or Excel. The
ReportService uses these strategies, allowing for flexible report format output without altering its own code

## Used Stack:

- **Development:** Java 17, Spring Boot 3.3.0
- **Build Tool:** Maven
- **Testing:** JUnit 5, AssertJ, Mockito
- **Code Simplification and Logging:** Lombok, SLF4J with Logback
- **Code Quality Tools:** Maven plugins (Checkstyle, PMD)
- **Test Coverage Tools:** JaCoCo Maven Plugin (aiming for 80% coverage)
- **CI/CD:** GitHub Actions
- **Monitoring:** Spring Boot Actuator
- **Mapping:** MapStruct
- **API Documentation:** Swagger, Javadoc
- **Database Migrations:** Liquibase
- **Database:** PostgreSQL, Elasticsearch
- **Message Broker:** Kafka
- **Monitoring:** Kibana

## How to run?

1) Clone repository

```shell
   git clone https://github.com/AndrewPleskanko/
```

2) Build project

```shell
   mvn clean install
```

3) Run project on Java

```shell
    cd block2
    mvn spring-boot:run
```

4) Use VM options to set the database connection details

 ```copy
-DDB_USERNAME=name
-DDB_PASSWORD=password
-DDB_HOST=localhost:5432
-DDB_NAME=dbName
-DMAIL_USERNAME=your_mail_username
-DMAIL_PASSWORD=your_mail_password
-DMAIL_HOST=syour_mail_host
-DMAIL_PORT=your_mail_port

-DKAFKA_ADDRESS=your_url
```

## How to test?

1) Open Swagger UI to test the endpoints

```copy
http://localhost:8080/swagger-ui.html
```

2) Use Postman collection to test the endpoints

```copy
ProfITsoft.postman_collection.json
```

3) Build project

```shell
   mvn clean install
   
   docker build -t mail-sender .
   docker-compose up -d
```

## Services Started

This command will start the following services:

- mail-sender: The Spring Boot application (available at http://localhost:8092)
- elasticsearch: Elasticsearch database
- kibana: Kibana for Elasticsearch (available at http://localhost:5601)
- zookeeper: Apache ZooKeeper for Kafka
- kafka: Apache Kafka message broker

## Additional Notes

The application uses port 8092 for the Spring Boot application, 9200 for Elasticsearch,
5601 for Kibana, 2181 for ZooKeeper, and 9092 for Kafka.



