package com.dev.james.routes

import com.dev.james.authentication.JwTConfig
import com.dev.james.data.authentication.UserRepository
import com.dev.james.data.todos.ToDoRepository
import com.dev.james.entities.ToDoDraft
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.createToDosRoutes(
    repository : ToDoRepository
){
    authenticate {
        get("/me"){
            val user = call.authentication.principal as JwTConfig.JwtUser
            call.respond(user)
        }

        get("/todos/{user_id}"){
            val userId = call.parameters["user_id"]
            if(userId == null){
                call.respond(HttpStatusCode.BadRequest , "No user id provided")
                return@get
            }
            call.respond(repository.getAllTodos(userId))
        }

        get("/todos/{user_id}/{id}"){
            val id = call.parameters["id"]?.toIntOrNull()
            val userId = call.parameters["user_id"]

            if(userId.isNullOrBlank()){
                call.respond(
                    HttpStatusCode.BadRequest ,
                    "No user id provided"
                )
                return@get
            }
            if(id == null){
                call.respond(HttpStatusCode.BadRequest , "No to do id found")
                return@get
            }

            val todo = repository.getToDo(id , userId)

            if(todo == null){
                call.respond(HttpStatusCode.NotFound , "No todo found that matches provided id")
                return@get
            }

            call.respond(todo)

        }

        post("/todos") {
            val todoDraft = call.receive<ToDoDraft>()
            val todo = repository.addToDo(todoDraft)
            call.respond(todo)
        }

        put("/todos/{id}"){

            val toDoId = call.parameters["id"]?.toIntOrNull()
            val todoDraft = call.receive<ToDoDraft>()

            if(toDoId == null){
                call.respond(
                    HttpStatusCode.BadRequest ,
                    "id parameter has to be a number"
                )
                return@put
            }
            val updated = repository.updateToDo(id = toDoId , draft = todoDraft)

            if(updated){
                call.respond(
                    HttpStatusCode.OK
                )
            }else{
                call.respond(
                    HttpStatusCode.NotFound ,
                    "found no todo  that matches provided id"
                )
            }
        }

        delete("/todos/{user_id}/{id}"){

            val toDoId = call.parameters["id"]?.toIntOrNull()
            val userId = call.parameters["user_id"]

            if(userId.isNullOrBlank()){
                call.respond(
                    HttpStatusCode.BadRequest ,
                    "No user id provided"
                )
                return@delete
            }

            if(toDoId == null){
                call.respond(
                    HttpStatusCode.BadRequest ,
                    "id parameter has to be a number"
                )
                return@delete
            }

            val result = repository.removeToDo(toDoId , userId)

            if(result){
                call.respond(
                    HttpStatusCode.OK
                )
            }else {
                call.respond(
                    HttpStatusCode.NotFound ,
                    "found no todo that matches provided id."
                )
            }

        }
    }
}