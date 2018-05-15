package io.corbs.todos

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository("todos")
interface TodosRepo : PagingAndSortingRepository<TodoEntity, Int> {
    fun findByTitleOrderById(title:String): List<TodoEntity>
}