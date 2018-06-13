package io.corbs.todos

data class TodoCreatedEvent(val todo: TodoEntity)

data class TodoDeletedEvent(val todo: TodoEntity)

data class TodoUpdatedEvent(val todo: TodoEntity)

