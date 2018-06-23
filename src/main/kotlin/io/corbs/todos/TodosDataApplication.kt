package io.corbs.todos

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@EnableCircuitBreaker
@EnableDiscoveryClient
@SpringBootApplication
@RestController
class TodosDataApplication(@Autowired val messaging: TodosMessaging) {

    @PostMapping("/event")
    fun fireEvent(@RequestBody todo: TodoEntity) {
        messaging.fireCreatedEvent(CreatedEvent(todo))
    }
}

fun main(args: Array<String>) {
    runApplication<TodosDataApplication>(*args)
}



















