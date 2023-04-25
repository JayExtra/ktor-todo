package com.dev.james.data.todos

import com.dev.james.entities.ToDo
import com.dev.james.entities.ToDoDraft

interface ToDoRepository {
    fun getAllTodos(userId : String) : List<ToDo>
    fun getToDo(id : Int , userId: String) : ToDo?
    fun addToDo(draft : ToDoDraft) : ToDo
    fun removeToDo(id : Int , userId : String) : Boolean
    fun updateToDo(id : Int , draft : ToDoDraft) : Boolean
}