<h1 align="center">
Explore With Me (pet project)
</h1>

'Explore With Me' is a graduation project at [Yandex.Practicum](https://practicum.yandex.ru) platform. 
It represents back-end logics for a social network that is intended to help people plan/attend common events. 
Users can create events, promote them and invite other users to participate in them.

## Structure:
Project consists of two modules:
* main service
* stats service (collects statistics for events public endpoints)

## Features:
* APIs are split into three levels: private, public, admin
* event endpoint view statistics are collected and stored on a dedicated stats server
* events are stored under categories which are created and managed by admins
* events are being created in pending status for admin to approve of reject them
* events can be collected into compilations that can be later pinned to user pages (created and managed by admins)
* wide set of filters for event search
* user can subscribe to other users to get list of events friends are attending 
* users can restrict subscription to them by activating private mode
* confirmation mode can be activated for user participation requests
* other features

## Tech stack:
Spring Boot, Maven, Hibernate, Querydsl, Lombok, PostgreSQL, Swagger, Postman, Docker

## Swagger specification:
* [Main service](https://raw.githubusercontent.com/EvgeniPolyakov/java-explore-with-me/main/ewm-main-service-spec.json)
* [Stats service](https://raw.githubusercontent.com/EvgeniPolyakov/java-explore-with-me/main/ewm-stats-service-spec.json)

## Postman tests:
* [Tests for main service](https://github.com/EvgeniPolyakov/java-explore-with-me/blob/main/postman/ewm-main-service.json)
* [Tests for stats service](https://github.com/EvgeniPolyakov/java-explore-with-me/blob/main/postman/ewm-stat-service.json)
* [Tests for subscription feature](https://github.com/EvgeniPolyakov/java-explore-with-me/blob/main/postman/ewm-subscription-tests.json) (should be run separately on an empty DB).


## Launch:
App can be launched by building and running four docker images (via docker-compose.yml):
* Main service
* Main service DB
* Stats service
* Stats service DB

## DB structure:
<p align="center"> Main service</p>

![image](https://raw.githubusercontent.com/EvgeniPolyakov/java-explore-with-me/main/service/src/main/resources/service-db.png)

<p align="center"> Stats service</p>

![image](https://raw.githubusercontent.com/EvgeniPolyakov/java-explore-with-me/main/stats/src/main/resources/stats-db.png)

