## Todo(s) Data

Howdy and welcome...data, data and more data, the world is full of data.  Inevitably you'll need to implement Microservices to talk with Databases.  This repository contains a Microservice implemented in [Kotlin](https://kotlinlang.org) using [Spring Boot](https://spring.io/projects/spring-boot) and [Spring Cloud](https://spring.io/projects/spring-cloud).  It highlights several unique features from Spring and integrates with the [Todo(s) EcoSystem](https://github.com/corbtastik/todo-ecosystem) of Microservices, which are used for a larger demo set.  Checkout the [Todo(s) EcoSystem](https://github.com/corbtastik/todo-ecosystem) to see how all the Microservices fit together.

### Primary dependencies

* Spring Boot Starter Web (Embedded Tomcat)
* Spring Boot Actuators (ops endpoints)
* Spring Cloud Netflix Eureka Client (service discovery)
* Spring Cloud Config Client (central config)
* Spring Cloud Sleuth (request tracing)
* Spring Data JPA (easy database access)
* Flyway (Database schema migration)
* Swagger (API documentation)

### Kotlin

[Spring 5](http://spring.io/) added support for a 3rd language in the Framework.  Developers can now implement Microservices in [Java](https://en.wikipedia.org/wiki/Java_(programming_language)), [Groovy](https://en.wikipedia.org/wiki/Apache_Groovy) and [Kotlin](https://kotlinlang.org).  Kotlin is slowly starting to show up in development communities given most like the buttoned up syntax.  If you're a Java developer you'll get up to speed on Kotlin quickly because it's very Java like but with lots of simplifications.

Kotlin is a statically typed open source language originated by [JetBrains](https://www.jetbrains.com) and now officially supported by Spring.  This Microservice leverages Kotlin to do what Java can do but with less code.  If you'd like to dig into Kotlin please look at [Todo(s) Kotlin](https://github.com/corbtastik/todos-kotlin) which goes into more depth on the language.  For the [Todo(s) Data Microservice](https://github.com/corbtastick/todos-data) we'll be focusing more on Spring Data, Spring Data Rest and Spring Cloud Streams, however we'll highlight effective Kotlin features where applicable.

Java is not going away anytime soon but as the future of software unfolds its highly likely another JVM language will close the gap.  Will the next generation of developers continue with Java or turn to something with less boilerplate?  No one has a crystal ball but Kotlin has a solid foundation and a nice start.  For one it's originated from JetBrains the maker of IntelliJ and they've been in the JVM market a long time and have the knowledge to implement something tight.  Kotlin is a nice fit for Microservices given both aim to simply software development.  I'd recommend giving it some attention and make an informed decision to use or not.  In any case I'd guess most Java developers will be pleasantly surprised using Kotlin...go ahead and give it a try, it's ``fun()``.

### Flyway - Database Migrations

This Microservice uses [Flyway](https://flywaydb.org/) to handle database schema versioning.  Using Flyway from Spring Boot starts with declaring a dependency on ``org.flywaydb:flyway-core`` in ``pom.xml``.  Spring Boot handles [Auto-Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/howto-database-initialization.html#howto-execute-flyway-database-migrations-on-startup) of Flyway on start-up when ``org.flyway:flyway-core`` is on the classpath.

Todo(s) Data uses a default Spring Boot/Flyway setup and overrides ``spring.flyway.locations`` for different profiles.  The ``default`` profile ``application.yml`` sets the location for H2 schema changes while the ``cloud`` profile sets the location for MySQL.

```bash
> tree ./src/main/resources
|____application-cloud.yml
|____application.yml
|____bootstrap.yml
|____db
| |____migration
| | |____h2
| | | |____V1.0.0__create_todos_table.sql
| | |____mysql
| | | |____V1.0.0__create_todos_table.sql
```

If for some reason you need to disable schema migration just override ``spring.flyway.enabled=false``, by default Spring Boot will run ``Flyway.migrate()`` on startup.

### Todo(s) Data JPA

One of the most underrated features of Spring is the [Spring Data family](https://spring.io/projects/spring-data).  Connecting Microservices to databases and stores is one of the cornerstone of the framework.  Giving developers a similar framework components to access data in a low touch manner.

Our model is the ``Todo`` and since we're using Flyway we're controlling the schema creation through migrations in ``src/main/resources/db/migrations``.  We could allow JPA to create the schema but we're choosing not too as most enterprise shops favor this level of separation.  This finer grained control comes with more responsibility on the developer.  We need to ensure our Object matches the schema, so as a safe guard we enable ``spring.jpa.hibernate.ddl-auto=validate`` which will [halt-and-catch-fire](https://en.wikipedia.org/wiki/Halt_and_Catch_Fire_(TV_series)) should those two be outta wack.

Simple objects is where Kotlin shines, ``data classes`` in Kotlin allow developers to put together POKO(s) using a tight syntax.  Data Classes have built-in getter/setter support, you simply define your model as a `` data class``.  If the type is a ``val`` its read-only thus getter support is baked into the class, if its ``var`` getter and setters will be present.  To get something equivalent in Java we'd have to use [Lombok](https://projectlombok.org/) otherwise we'd have a lot of boilerplate code to write.

#### Kotlin data class

```kotlin
@Entity
@Table(name="todos")
data class TodoEntity(val title: String, val completed: Boolean) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null
    constructor() : this("",false)
}
```

### Todo(s) Data as a StreamListener

Todo(s) Data Microservice also functions as a ``@StreamListener`` and will listen for events and handle them by persisting Todo(s) in a backing SQL database.

#### Sweet Sweet Kotlin data classes

```kotlin
# look mom no semicolons and 3 objects in 3 lines :)
data class CreatedEvent(val todo: TodoEntity)
data class DeletedEvent(val todo: TodoEntity)
data class UpdatedEvent(val todo: TodoEntity)
```
### Todo(s) Data Rest API

Todo(s) Data Microservice also uses ``spring-boot-starter-data-rest`` to blanked our data model in a REST API, one that supports paging and sorting (whoop-whoop).

```kotlin
@Repository("todos")
interface TodosRepo : PagingAndSortingRepository<TodoEntity, Int> {
    fun findByTitleOrderById(title:String): List<TodoEntity>
}
```

### Build

```bash
git clone https://github.com/corbtastik/todos-data.git
cd todos-data
./mvnw clean package
```

### Run

```bash
java -jar target/todos-data-1.0.0.SNAP.jar
```

### Run with Remote Debug

```bash
java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=9111,suspend=n \
  -jar target/todos-data-1.0.0.SNAP.jar
```

### Verify

By default Todo(s) Data starts up on ``localhost:8003`` and like all Microservices in [Todo(s) EcoSystem](https://github.com/corbtastik/todo-ecosystem) actuators, Discovery Client and Config Client are baked into the app.

#### Check versioning

```bash
>  http :8003/ops/info
HTTP/1.1 200 
Content-Type: application/vnd.spring-boot.actuator.v2+json;charset=UTF-8
Date: Fri, 22 Jun 2018 19:53:34 GMT
Transfer-Encoding: chunked

{
    "build": {
        "artifact": "todos-data",
        "group": "io.corbs",
        "name": "todos-data",
        "time": "2018-06-22T19:48:26.360Z",
        "version": "1.0.0.SNAP"
    }
}
```

#### Check root API

```bash
>  http :8003/        
HTTP/1.1 200 
Content-Type: application/hal+json;charset=UTF-8
Date: Fri, 22 Jun 2018 19:54:31 GMT
Transfer-Encoding: chunked

{
    "_links": {
        "profile": {
            "href": "http://localhost:8003/profile"
        },
        "todoEntities": {
            "href": "http://localhost:8003/todoEntities{?page,size,sort}",
            "templated": true
        }
    }
}
```

#### Spring Data Rest CRUD ops

**Create**
```bash
> http :8003/todoEntities/ title="make bacon pancakes"
HTTP/1.1 201 
Content-Type: application/json;charset=UTF-8
Date: Fri, 22 Jun 2018 19:56:55 GMT
Location: http://localhost:8003/todoEntities/1
Transfer-Encoding: chunked

{
    "_links": {
        "self": {
            "href": "http://localhost:8003/todoEntities/1"
        },
        "todoEntity": {
            "href": "http://localhost:8003/todoEntities/1"
        }
    },
    "completed": false,
    "title": "make bacon pancakes"
}
```

Open ``localhost:8003/h2-console`` and connect to H2 to view TODOS table.

<p align="center">
    <img src="https://github.com/corbtastik/todos-images/raw/master/todos-data/h2console.png">
</p>

**Retrieve**

```bash
> http :8003/todoEntities/1                
HTTP/1.1 200 
Content-Type: application/hal+json;charset=UTF-8
Date: Fri, 22 Jun 2018 20:06:39 GMT
Transfer-Encoding: chunked

{
    "_links": {
        "self": {
            "href": "http://localhost:8003/todoEntities/1"
        },
        "todoEntity": {
            "href": "http://localhost:8003/todoEntities/1"
        }
    },
    "completed": false,
    "title": "make bacon pancakes"
}
```

**Update**
```bash
> http PATCH :8003/todoEntities/1 completed=true 
HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Date: Fri, 22 Jun 2018 20:07:12 GMT
Transfer-Encoding: chunked

{
    "_links": {
        "self": {
            "href": "http://localhost:8003/todoEntities/1"
        },
        "todoEntity": {
            "href": "http://localhost:8003/todoEntities/1"
        }
    },
    "completed": true,
    "title": "make bacon pancakes"
}
```

**Delete**
```bash
> http DELETE :8003/todoEntities/1        
HTTP/1.1 204 
Date: Fri, 22 Jun 2018 20:07:59 GMT

> http :8003/todoEntities/1                     
HTTP/1.1 404 
Content-Length: 0
Date: Fri, 22 Jun 2018 20:08:05 GMT
```

#### Verify Event Listening

Todo(s) Data is also an Event Listener, we use Spring Cloud Streams to bind to the underlying Messaging vertebra which enables this Microservice to listen and respond to Todo Events.

**Install and Run RabbitMQ**

To test if that's working we'll need to start RabbitMQ locally.  If you're using OSX RabbitMQ can be installed by homebrew with one command ``brew install rabbitmq``.  Once installed run ``rabbitmq-server`` to start the messaging backbone.

**Fire and Handle Events**

The Todo(s) Data Microservice exposes one HTTP endpoint that will fire an Event so we can test Event Handling.  To test we can ``POST`` a ``Todo`` to ``localhost:8003/event`` which will fire a ``CreatedEvent``, which will be handled by ``@StreamListener("onCreatedEvent")``.

```bash
> http POST :8003/event title="make bacon pancakes event"
HTTP/1.1 200 
Content-Length: 0
Date: Sat, 23 Jun 2018 17:13:19 GMT
```

**Logging Events**

After ``POST``ing a ``Todo`` you should see logging events for ``fireCreatedEvent`` and ``onCreatedEvent``.

```kotlin
fun fireCreatedEvent(todo: CreatedEvent) {
    LOG.debug("todos.data fireCreatedEvent: " + todo.toString())
    this.channels.fireCreatedEvent().send(GenericMessage<Any>(todo))
}

@StreamListener("onCreatedEvent")
fun onCreatedEvent(event: CreatedEvent) {
    if (ObjectUtils.isEmpty(event.todo)) {
        return
    }
    LOG.debug("todos.data onCreatedEvent: " + event.todo.toString())
    this.repo.save(event.todo)
}
```

```bash
2018-06-23 12:13:19.171 DEBUG [todos-data,58fcabc08955a896,58fcabc08955a896,false] 89058 --- [nio-8003-exec-9] io.corbs.todos.TodosMessaging : todos.data fireCreatedEvent: CreatedEvent(todo=TodoEntity(title=make bacon pancakes event, completed=false))
2018-06-23 12:13:19.173 DEBUG [todos-data,58fcabc08955a896,d1f11107b3607fe2,false] 89058 --- [bqtDc0aLCUugA-1] io.corbs.todos.TodosMessaging : todos.data onCreatedEvent: TodoEntity(title=make bacon pancakes event, completed=false)
```

**Verify Todo event landed in DB**

``POST``ing to ``/event`` result in a ``CreatedEvent`` being fired and handled by ``TodosMessaging.onCreatedEvent`` which saves the ``Todo`` in a backing database.  The Microservice starts with the default profile which saves to H2.  Check the database for the ``Todo``.

Open ``localhost:8003/h2-console`` and connect to H2 to view TODOS table.

<p align="center">
    <img src="https://github.com/corbtastik/todos-images/raw/master/todos-data/h2console-event.png">
</p>

