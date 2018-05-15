package io.corbs.todos

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.annotation.Input
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.messaging.SubscribableChannel
import org.springframework.util.ObjectUtils

@EnableCircuitBreaker
@EnableDiscoveryClient
@SpringBootApplication
@EnableBinding(TodosDataApplication.SinkChannels::class)
class TodosDataApplication(@Autowired val repo: TodosRepo) {

    companion object { val LOG = LoggerFactory.getLogger(TodosDataApplication::class.java.name) }

    interface SinkChannels {
        @Input
        fun todoCreatedEvent(): SubscribableChannel

        @Input
        fun todoUpdatedEvent(): SubscribableChannel

        @Input
        fun todoDeletedEvent(): SubscribableChannel
    }

    @StreamListener("todoCreatedEvent")
    fun onCreatedEvent(event: TodoCreatedEvent) {
        if (ObjectUtils.isEmpty(event.todo.id)) {
            return
        }
        LOG.debug("caching todo " + event.todo.toString())
        this.repo.save(event.todo)
    }

    @StreamListener("todoUpdatedEvent")
    fun onUpdatedEvent(event: TodoUpdatedEvent) {
        if (ObjectUtils.isEmpty(event.todo.id)) {
            return
        }
        LOG.debug("updating todo " + event.todo.toString())
        this.repo.save(event.todo)
    }

    @StreamListener("todoDeletedEvent")
    fun onDeletedEvent(event: TodoDeletedEvent) {
        if (!ObjectUtils.isEmpty(event.todo.id)) {
            LOG.debug("removing todo " + event.todo.id)
            this.repo.deleteById(event.todo.id!!)
        } else {
            LOG.debug("removing all todo(s)")
            this.repo.deleteAll()
        }
    }

}

fun main(args: Array<String>) {
    runApplication<TodosDataApplication>(*args)
}


