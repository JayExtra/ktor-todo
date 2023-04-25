package com.dev.james.data.todos

import com.dev.james.entities.ToDo
import com.dev.james.entities.ToDoDraft

class InMemoryTodoRepo : ToDoRepository {
    private val todos =  mutableListOf<ToDo>()

    override fun getAllTodos(userId : String): List<ToDo> {
       return todos
    }

    override fun getToDo(id: Int , userId: String): ToDo? {
       return todos.firstOrNull { it.id == id }
    }

    override fun addToDo(draft: ToDoDraft): ToDo {
        val todo = ToDo(
            title = draft.title ,
            done = draft.done ,
            id = todos.size + 1 ,
            user_id = draft.user_id
        )
        todos.add(todo)
        return todo
    }

    override fun removeToDo(id: Int , userId: String): Boolean {
        return todos.removeIf { it.id == id }
    }

    override fun updateToDo(id: Int, draft: ToDoDraft): Boolean {
        val todo = todos.firstOrNull { it.id == id } ?: return false

        todo.title = draft.title
        todo.done = draft.done

        return true
    }
}