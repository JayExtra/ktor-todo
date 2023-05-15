package com.dev.james.domain.repository

import com.dev.james.domain.models.ToDo
import com.dev.james.domain.models.ToDoDraft


interface ToDoRepository {
    suspend fun getAllTodos(userId : String) : List<ToDo>
    suspend fun getToDo(id : Int , userId: String) : ToDo?
    suspend fun addToDo(draft : ToDoDraft) : ToDo
    suspend fun removeToDo(id : Int , userId : String) : Boolean
    suspend fun updateToDo(id : Int , draft : ToDoDraft) : Boolean
}