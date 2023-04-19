package com.dev.james

import com.dev.james.authentication.JwTConfig
import com.dev.james.data.authentication.InMemoryUserRepository
import com.dev.james.data.authentication.UserRepository
import com.dev.james.data.todos.MySqlToDoRepository
import com.dev.james.data.todos.ToDoRepository
import com.dev.james.entities.LoginBody
import com.dev.james.entities.ToDoDraft
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

val jwtConfig = JwTConfig(System.getenv("KTOR_TODOLIST_JWT_SECRET"))
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {


    /*

    TODO:
        1. Update this backend to support sign up and saving of user credentials in the database.
        2. Support hashing and salting of user password
        3. Put support for token expiry and token refresh
        4. Research and add support for structured concurrency with Coroutines.
    */

    install(ContentNegotiation) {
        gson {

        }
    }

    install(CallLogging)

    install(Authentication){
        jwt {
            jwtConfig.configureKtorFeature(this)
        }
    }

    routing {

        val repository : ToDoRepository = MySqlToDoRepository()
        val userRepository : UserRepository = InMemoryUserRepository()

        get("/") {
            call.respondText("HELLO TODO BACKEND!")
        }

        post("/login") {
            val loginBody = call.receive<LoginBody>()

            val user = userRepository.loginUser(loginBody.username , loginBody.password)

            if(user == null){
                call.respond(HttpStatusCode.Unauthorized , "Invalid credentials")
                return@post
            }

            val token = jwtConfig.generateToken(JwTConfig.JwtUser(user.userId , user.username))

            call.respond(token)
        }

        authenticate {

            get("/me"){
                val user = call.authentication.principal as JwTConfig.JwtUser
                call.respond(user)
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
}

