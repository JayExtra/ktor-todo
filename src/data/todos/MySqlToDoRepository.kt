package com.dev.james.data.todos

import com.dev.james.database.DatabaseManager
import com.dev.james.entities.ToDo
import com.dev.james.entities.ToDoDraft

class MySqlToDoRepository : ToDoRepository {
    private val database = DatabaseManager()

    override fun getAllTodos(): List<ToDo> {
        return database.getAllToDos()
            .map { ToDo(it.id, it.title, it.done) }
    }

    override fun getToDo(id: Int): ToDo? {
        return database.getTodo(id)?.let {
            ToDo(
                it.id,
                it.title,
                it.done
            )
        }
    }

    override fun addToDo(draft: ToDoDraft): ToDo {
        return database
            .addTodo(draft)
    }

    override fun removeToDo(id: Int): Boolean {
        return database.removeToDo(id)
    }

    override fun updateToDo(id: Int, draft: ToDoDraft): Boolean {
        return database.updateTodo(
            id,
            draft
        )
    }
}