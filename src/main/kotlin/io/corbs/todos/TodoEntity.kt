package io.corbs.todos

import javax.persistence.*

@Entity
@Table(name="todos")
data class TodoEntity(val title: String, val completed: Boolean) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null
    constructor() : this("",false)
}
