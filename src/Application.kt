package com.dev.james

import com.dev.james.data.InMemoryTodoRepo
import com.dev.james.data.MySqlToDoRepository
import com.dev.james.data.ToDoRepository
import com.dev.james.entities.ToDo
import com.dev.james.entities.ToDoDraft
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.gson.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(Authentication) {
        basic("myBasicAuth") {
            realm = "Ktor Server"
            validate { if (it.name == "test" && it.password == "password") UserIdPrincipal(it.name) else null }
        }
    }

    install(ContentNegotiation) {
        gson {

        }
    }

    install(CallLogging)

    routing {

        val repository : ToDoRepository = MySqlToDoRepository()

        get("/") {
            call.respondText("HELLO TODO BACKEND!")
        }

        get("/todos"){
           call.respond(repository.getAllTodos())
        }

        get("/todos/{id}"){
            val id = call.parameters["id"]?.toIntOrNull()
            if(id == null){
                call.respond(HttpStatusCode.BadRequest , "No user id found")
                return@get
            }
            val todo = repository.getToDo(id)

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

        delete("/todos/{id}"){

            val toDoId = call.parameters["id"]?.toIntOrNull()

            if(toDoId == null){
                call.respond(
                    HttpStatusCode.BadRequest ,
                    "id parameter has to be a number"
                )
                return@delete
            }

            val result = repository.removeToDo(toDoId)

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

