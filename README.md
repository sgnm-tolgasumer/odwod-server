# Worderize Server

- This is the backend side of the project.
- Quarkus with Apache Kafka used in the project.
- Server is running on the Docker cluster with 3 containers (Kafka, Kafka Zookeper, MySQL).
- Java is used as a language.
- Database operations handled with Panache Hibernate ORM.
- REST API is created for CRUD operations.

## Live Demo Server on Google Cloud
http://worderize.codes

## Poster of the Project
![POSTER_LAST](https://user-images.githubusercontent.com/34353055/117486602-81939f00-af72-11eb-8784-dd728e4dfa31.jpeg)

### What is Worderize
A work order is a task or job that can be scheduled and assigned to someone who wants to get this job. Worderize is a platform in which customers can create and workers can get the work orders. Worderize brings customers and workers together to manage work orders in an easy way. 

Registered workers can select a "worder" from filtered work orders list according to their job type and location preferences. Later on, they can change these preferences with updating their profiles.

The whole process can be monitored by customers so they can track their work orders via application. They can create multiple work orders and manage each of them.

Worderize developed for web platform with the most recent technologies in Angular Framework and also using Apache Kafka to data streaming with the Quarkus in the backend.


### Features and Benefits
- Quick and easy work order creation
- Track & find work orders
- Various job types
- Different locations
- Filtered work orders by worker's preferences
- Creates a common platform
- Easy to use web application
- 24/7 support

## Block Diagram of the Project
![block-diagram-poster](https://user-images.githubusercontent.com/34353055/117164490-558ae900-adcd-11eb-8ff5-c24be3b96e0d.png)
 
### Followed Code Standard for Java Language
https://www.oracle.com/java/technologies/javase/codeconventions-contents.html

## How to Compile & Run

Run the required Kafka, Zookeper and MySQL versions on the docker: 
```shell script
docker-compose up --build
```
Run the Quarkus project with development mode:
```shell script
mvn quarkus:dev
```
