package io.corbs.todos

import javax.persistence.*

@Entity
@Table(name="todos")
data class TodoEntity(var title: String, var completed: Boolean) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
    constructor() : this("",false)
}
