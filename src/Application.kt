package com.dev.james

import com.dev.james.authentication.JwTConfig
import com.dev.james.domain.repository.UserRepository
import com.dev.james.data.authentication_security.SHA256HashingService
import com.dev.james.data.repository_impl.MySqlToDoRepository
import com.dev.james.data.repository_impl.MySqlUserRepository
import com.dev.james.domain.repository.ToDoRepository
import com.dev.james.routes.createAuthRoutes
import com.dev.james.routes.createToDosRoutes
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)


val jwtConfig = JwTConfig(System.getenv("KTOR_TODOLIST_JWT_SECRET"))

//TODO 2 : Look at doing tests in KTOR
//TODO 3: Research and add dependency injection with Koin

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    install(ContentNegotiation){
        gson()
    }

    install(CallLogging)

    install(Authentication){
        jwt {
            jwtConfig.configureKtorFeature(this)
        }
    }

    routing {

        val todosRepository : ToDoRepository = MySqlToDoRepository()
        val userRepository : UserRepository = MySqlUserRepository()
        val hashingService = SHA256HashingService()

        get("/") {
            call.respondText("HELLO TODO BACKEND!")
        }

        createAuthRoutes(
            userRepository = userRepository ,
            hashingService = hashingService
        )

        createToDosRoutes(
            repository = todosRepository
        )

    }
}

