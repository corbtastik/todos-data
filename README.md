## Todo(s) Data

Howdy and welcome...data, data and more data, the world is full of data.  This means inevitably you will need to implement Microservices to talk with Databases.  This repository contains a Microservice implemented in [Kotlin](https://kotlinlang.org) using Spring Boot and Spring Cloud.  It highlights several unique features from Spring and ties in with the [Todo(s) EcoSystem](https://github.com/corbtastik/todo-ecosystem) of Microservices, which are used for a larger demo set.  Checkout the [Todo(s) EcoSystem](https://github.com/corbtastik/todo-ecosystem) to see how all the Microservices fit together.

### Primary dependencies

* Spring Boot Starter Web (Embedded Tomcat)
* Spring Boot Actuators (ops endpoints)
* Spring Cloud Netflix Eureka Client (service discovery)
* Spring Cloud Config Client (central config)
* Spring Cloud Sleuth (request tracing)
* Spring Data JPA (easy database access)
* Swagger (API documentation)

### Kotlin

[Spring 5](http://spring.io/) added support for a 3rd language in the Framework.  Developers can now implement Microservices in [Java](https://en.wikipedia.org/wiki/Java_(programming_language)), [Groovy](https://en.wikipedia.org/wiki/Apache_Groovy) and [Kotlin](https://kotlinlang.org) using the same framework.  Kotlin is slowly starting to show up in development communities given most developers like the buttoned up syntax.  If you're a Java developer you'll get up to speed on Kotlin quickly because it's very Java like but with lots of simplifications.

Kotlin is a statically typed open source language originated by [JetBrains](https://www.jetbrains.com) and now officially supported by Spring.  This Microservice leverages Kotlin to do what Java can do but with less code.  If you'd like to dig into Kotlin please look at [Todo(s) Kotlin](https://github.com/corbtastik/todos-kotlin) which goes into more depth on the language.  For the [Todo(s) Data Microservice](https://github.com/corbtastick/todos-data) we'll be focusing more on Spring Data, Spring Data Rest and Spring Cloud Streams, however we'll highlight effective Kotlin features where applicable.

Java is not going away anytime soon but as the future of software unfolds its highly likely another JVM language will close the gap over the next 5-10 years.  Will the next generation of developers continue with Java or turn to something with less ceremony?  No one has a crystal call but Kotlin has a solid foundation and a nice start.  For one it's originated from JetBrains the maker of IntelliJ and they've been in the JVM market a long time and have the knowledge to implement something tight.  Kotlin may just be a great fit for Microservices given both aim to simply software development.  I'd recommend giving it some attention and make an informed decision where to use or not.  In any case I'd guess most Java developers will be pleasantly surprised using Kotlin...go ahead and give it a try.


