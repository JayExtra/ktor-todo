package com.dev.james

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.auth.*
import io.ktor.features.*

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

    }

    routing {
        get("/") {
            call.respondText("HELLO TODO BACKEND!")
        }

        get("/todos"){

        }

        get("/todos/{id}"){
            val id = call.parameters["id"]
            call.respondText("Todolist details for ToDo Item #$id")
        }

        post("/todos") {

        }
        put("/todos/{id}"){

        }
        delete("/todos/{id}"){

        }
    }
}

