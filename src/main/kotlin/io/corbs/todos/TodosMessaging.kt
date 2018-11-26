package io.corbs.todos

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.annotation.Input
import org.springframework.cloud.stream.annotation.Output
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.SubscribableChannel
import org.springframework.messaging.support.GenericMessage
import org.springframework.stereotype.Component
import org.springframework.util.ObjectUtils
import org.springframework.util.StringUtils

data class CreatedEvent(val todo: TodoEntity)
data class DeletedEvent(val todo: TodoEntity)
data class UpdatedEvent(val todo: TodoEntity)

@Component
@EnableBinding(TodosMessaging.Channels::class)
class TodosMessaging(
    @Autowired val repo: TodosRepo,
    @Autowired val channels: TodosMessaging.Channels) {

    companion object { val LOG = LoggerFactory.getLogger(TodosMessaging::class.java.name) }

    interface Channels {
        @Input
        fun onCreatedEvent(): SubscribableChannel

        @Input
        fun onUpdatedEvent(): SubscribableChannel

        @Input
        fun onDeletedEvent(): SubscribableChannel

        @Output
        fun fireCreatedEvent(): MessageChannel
    }

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

    @StreamListener("onUpdatedEvent")
    fun onUpdatedEvent(event: UpdatedEvent) {
        if (ObjectUtils.isEmpty(event.todo.id)) {
            return
        }
        val result = repo.findById(event.todo.id!!)
        if(result.isPresent) {
            val current = result.get()
            if(!ObjectUtils.isEmpty(event.todo.completed)) {
                current.completed = event.todo.completed
            }
            if (!StringUtils.isEmpty(event.todo.title)) {
                current.title = event.todo.title
            }
            LOG.debug("todos.data onUpdatedEvent: todo.id=${current.id}, " + event.todo.toString())
            this.repo.save(event.todo)
        }
    }

    @StreamListener("onDeletedEvent")
    fun onDeletedEvent(event: DeletedEvent) {
        if (!ObjectUtils.isEmpty(event.todo.id)) {
            LOG.debug("todos.data handled onDeletedEvent: " + event.todo.toString())
            this.repo.deleteById(event.todo.id!!)
        } else {
            LOG.debug("todos.data handled onDeleteEvent ALL: " + event.todo.toString())
            this.repo.deleteAll()
        }
    }

}