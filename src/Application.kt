package com.dev.james

import com.dev.james.authentication.JwTConfig
import com.dev.james.data.authentication.MySqlUserRepository
import com.dev.james.data.authentication.UserRepository
import com.dev.james.data.authentication.security.SHA256HashingService
import com.dev.james.data.authentication.security.SaltedHash
import com.dev.james.data.todos.MySqlToDoRepository
import com.dev.james.data.todos.ToDoRepository
import com.dev.james.entities.*
import com.dev.james.utilities.generateRandomUid
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
        val userRepository : UserRepository = MySqlUserRepository()
        val hashingService = SHA256HashingService()

        get("/") {
            call.respondText("HELLO TODO BACKEND!")
        }

        post("/login") {
            val loginBody = call.receive<LoginBody>()

            val user = userRepository.loginUser(loginBody.email , loginBody.password)

            if(user == null){
                call.respond(HttpStatusCode.Unauthorized , "Invalid credentials")
                return@post
            }

            val isValidPassword = hashingService.verify(
                value = loginBody.password ,
                saltedHash = SaltedHash(
                    hash = user.password ,
                    salt = user.salt
                )
            )

            if( !isValidPassword ){
                call.respond(HttpStatusCode.Conflict , "Incorrect password or username")
                return@post
            }

            val token = jwtConfig.generateToken(JwTConfig.JwtUser(user.userId , user.username))

            val loginResponse = LoginResponseBody(
                token = token ,
                refresh_token = user.refreshToken,
                user = UserDetails(
                    email = user.email ,
                    password = user.password ,
                    user_id = user.userId ,
                    username = user.username
                )
            )

            call.respond(loginResponse)

        }

        post("/signup"){

            val credentials = call.receive<SignUpBody>()

            val isEmailEmpty = credentials.email.isEmpty()
            val isEmailCorrect = credentials.email.contains("@")
            val isPasswordEmpty = credentials.password.isEmpty()
            val isUsernameEmpty = credentials.username.isEmpty()

            if(!isEmailCorrect || isEmailEmpty || isPasswordEmpty || isUsernameEmpty){
                call.respond(HttpStatusCode.Conflict , "Ensure that all credentials are correct")
                return@post
            }

            val saltedHash = hashingService.generateSlatedHash(
                value = credentials.password
            )

            val createUserAction = userRepository.signUpUser(
                user = UserRepository.User(
                    userId = generateRandomUid(10) ,
                    email = credentials.email ,
                    username = credentials.username ,
                    salt = saltedHash.salt ,
                    password = saltedHash.hash ,
                    refreshToken = generateRandomUid(60)
                )
            )

            if(!createUserAction){
                call.respond(HttpStatusCode.InternalServerError , "Cannot signup user at the moment , please try again later")
                return@post
            }

            call.respond(HttpStatusCode.OK , "User signed up successfully.")

        }

        post("/refresh_token") {
            val refreshTokenBody = call.receive<RefreshTokenBody>()

            if(refreshTokenBody.refresh_token.isEmpty()){
                call.respond(HttpStatusCode.BadRequest , "refresh token is required to get a new token.")
                return@post
            }

            if(refreshTokenBody.email.isEmpty()){
                call.respond(HttpStatusCode.BadRequest , "your email is required to get a new token.")
                return@post
            }

            val user = userRepository.getUser(refreshTokenBody.email)

            if(user == null){
                call.respond(HttpStatusCode.Conflict , "User with this email does not exist")
                return@post
            }

            if(user.refreshToken != refreshTokenBody.refresh_token){
                call.respond(HttpStatusCode.Conflict , "Refresh token provided does not exist.")
                return@post
            }

            val newToken = jwtConfig.generateToken(JwTConfig.JwtUser(userId = user.userId , userName = user.username))

            call.respond(newToken)

        }

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
}

