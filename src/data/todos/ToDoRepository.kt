package com.dev.james.data.todos

import com.dev.james.entities.ToDo
import com.dev.james.entities.ToDoDraft

interface ToDoRepository {
    suspend fun getAllTodos(userId : String) : List<ToDo>
    suspend fun getToDo(id : Int , userId: String) : ToDo?
    suspend fun addToDo(draft : ToDoDraft) : ToDo
    suspend fun removeToDo(id : Int , userId : String) : Boolean
    suspend fun updateToDo(id : Int , draft : ToDoDraft) : Boolean
}