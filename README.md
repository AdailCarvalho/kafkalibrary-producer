# kafkalibrary-producer

Sends book information collected into a scanner to a Kafka cluster

## Requirements

- JDK 11 (recommended)

## Build

Run the following command inside the project's root directory:

```sh
gradlew clean build
```

If the build success, a jar file named *kafkalibrary-producer-[version].jar* 
will be created into the *build/libs* directory.

## Start Application

Start up the application using the following command:

```sh
java -jar kafkalibrary-producer-[version].jar
```

You can also start up running the following command:

```sh
./gradlew bootRun
```

## Usage scenarios

### Scenario 1 - Sending data to topic library-events

- CURL

```json
curl --location --request POST 'localhost:8080/v1/libraryevent' \
--header 'Content-Type: application/json' \
--data-raw '{
    "libraryEventId": null,
    "libraryEventType": "NEW",
    "book": {
        "bookId": 1,
        "bookName": "The Art of War",
        "bookAuthor": "Sun Tzu"
    }
}'
```

- API Result:

```json
{
    "libraryEventId": null,
    "libraryEventType": "NEW",
    "book": {
        "bookId": 1,
        "bookName": "The Art of War",
        "bookAuthor": "Sun Tzu"
    }
}
```

- Logs:

```sh
Message sent successfully for the key null and value { "libraryEventId": null, "libraryEventsType": "NEW",
{"bookId":1,"bookName":"The Art of War","bookAuthor":"Sun Tzu"}}
partition: 1
```

## Commom Issues

**Problem**: *Error while loading log dir kafka*.

**Solution**: Change the bellow log properties into *server.properties* file to avoid log retention errors. This may cause you
broker startup to fail. Note that this must only be edited in development environments. Applying this to production will
cause data loss!


```sh
# The minimum age of a log file to be eligible for deletion due to age
log.retention.hours=-1
log.cleaner.enable=false
```