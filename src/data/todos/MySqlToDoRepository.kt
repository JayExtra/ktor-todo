package com.dev.james.data.todos

import com.dev.james.database.DatabaseManager
import com.dev.james.entities.ToDo
import com.dev.james.entities.ToDoDraft

class MySqlToDoRepository : ToDoRepository {
    private val database = DatabaseManager()

    override fun getAllTodos(userId : String): List<ToDo> {
        return database.getAllToDosByUserId(userId)
            .map { ToDo(it.id, it.title, it.done , it.user_id) }
    }

    override fun getToDo(id: Int , userId: String): ToDo? {
        return database.getTodo(id , userId)?.let {
            ToDo(
                it.id,
                it.title,
                it.done ,
                it.user_id
            )
        }
    }

    override fun addToDo(draft: ToDoDraft): ToDo {
        return database
            .addTodo(draft)
    }

    override fun removeToDo(id: Int , userId: String): Boolean {
        return database.removeToDo(id , userId)
    }

    override fun updateToDo(id: Int, draft: ToDoDraft): Boolean {
        return database.updateTodo(
            id,
            draft
        )
    }
}