# Money Tracker
The Money Tracker is an application to track your monthly expenses. It provides a true RESTful HTTP API, which is documented using Spring Rest Docs.

Visit `http://localhost:8080` to use the application.

Visit `http://localhost:8080/docs/api-guide.html` to view the documentation.

## Build and Run Locally [![Build Status](https://travis-ci.org/dennisstritzke/money-tracker.svg?branch=master)](https://travis-ci.org/dennisstritzke/money-tracker)
```
mvn package
java -jar target/*.jar
```

## Run in Docker
```
docker-compose up -d
```

Visit [http://localhost:8080](http://localhost:8080).

## Run in OpenShift
```
oc new-app --template=postgresql-persistent -p=POSTGRESQL_DATABASE=moneytracker
oc new-app dstritzke/money-tracker:1.0.2
oc patch dc money-tracker -p '{
  "spec": {
    "template": {
      "spec": {
        "containers": [{
          "name":"money-tracker",
          "resources": {
            "limits": {
              "memory": "512Mi"
            },
            "requests": {
              "memory": "256Mi"
            }
          },
          "readinessProbe": {
            "httpGet": {
              "path": "/health",
              "port": 8080,
              "scheme": "HTTP"
            }
          },
          "env": [{
              "name":"SPRING_DATASOURCE_URL",
              "value":"jdbc:postgresql://postgresql:5432/moneytracker"
            }, {
              "name":"SPRING_DATASOURCE_USERNAME",
              "valueFrom": {
                "secretKeyRef": {
                  "key": "database-user",
                  "name":"postgresql"}}
            }, {
              "name":"SPRING_DATASOURCE_PASSWORD",
              "valueFrom": {
                "secretKeyRef": {
                  "key": "database-password",
                  "name":"postgresql"}}
            }, {
              "name":"SPRING_JPA_HIBERNATE_DDL_AUTO",
              "value":"update"
            }]}]}}}}'
oc create route edge --service=money-tracker --insecure-policy=Redirect
echo "Visit https://$(oc get route money-tracker -o jsonpath='{.spec.host}')"
```

## Known Vulnerability Scan
Within the pom.xml the OWASP dependency check is added. To execute the dependency scan, execute `mvn dependency-check:aggregate`.
