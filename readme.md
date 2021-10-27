# Applikation starten

Um den postgres db docker zu starten:

```
docker-compose up
```

Um die Applikation zu starten:

```
mvn clean spring-boot:run
```

oder:

```
java -jar target/todoproj-1.0.0.jar
```

(hierfür ist einmalig mvn clean package nötig)

# Requests

Die implementierten requests sind die folgenden:

## GET localhost:8080/api/v1/tasks/
liefert alle Tasks

## GET localhost:8080/api/v1/tasks/{id}
liefert Task mit der gegebenen id, falls er existiert

## POST localhost:8080/api/v1/tasks
erzeugt neuen Task,
liefert erstellten Task zurück.
### Parameter:
- title:String - Titel des Tasks
- isDone:boolean (opt.) - Task erledigt? J/N

## PATCH localhost:8080/api/v1/tasks/{id}
updatet bestehenden Task mit der gegebenen id, falls ein solcher existiert
liefert veränderten Task zurück.
### Parameter:
- title:String (opt.) - Titel des Tasks
- isDone:boolean (opt.) - Task erledigt? J/N