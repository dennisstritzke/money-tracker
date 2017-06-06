# Money Tracker
The Money Tracker is an application to track your monthly expenses. It provides a true RESTful HTTP API, which is documented using Spring Rest Docs.

Visit `http://localhost:8080` to use the application.

Visit `http://localhost:8080/docs/api-guide.html` to view the documentation.

## Build [![Build Status](https://travis-ci.org/dennisstritzke/money-tracker.svg?branch=master)](https://travis-ci.org/dennisstritzke/money-tracker)
```
mvn package
java -jar target/*.jar
```

## Run with Docker
```
docker run -p 8080:8080 --name moneytracker -d dstritzke/money-tracker:latest
```

Visit [http://localhost:8080](http://localhost:8080).

## Known Vulnerability Scan
Within the pom.xml the OWASP dependency check is added. To execute the dependency scan, execute `mvn dependency-check:aggregate`.
