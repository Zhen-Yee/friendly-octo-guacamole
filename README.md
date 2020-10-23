# Supported Jukebox API
## Description

This API will return a list of jukeboxes that supports certain setting which the user provides in the form of a `settingId`. Other field that is supported by this API, but optional, are:
- `model` (model of a jukebox)
- `offset` (desired page index)
- `limit` (number of jukeboxes per page)

## Requirements

Make sure all dependencies have been installed before moving on.

* [Maven](https://maven.apache.org/download.cgi)
* [JDK - Java 11](https://www.oracle.com/java/technologies/javase-downloads.html)

## Local development setup

1. Clone the project: `$ git clone https://github.com/Zhen-Yee/friendly-octo-guacamole.git`
2. Run `mvn clean install`.
3. Run `mvn spring-boot:run` to start the project on http://localhost:8080

## API Documentation
Visit http://localhost:8080/swagger-ui.html to view API Documentation
