package com.dev.james.routes

import com.dev.james.authentication.JwTConfig
import com.dev.james.data.authentication.UserRepository
import com.dev.james.data.authentication.security.HashingService
import com.dev.james.data.authentication.security.SaltedHash
import com.dev.james.entities.*
import com.dev.james.jwtConfig
import com.dev.james.utilities.generateRandomUid
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.createAuthRoutes(
    userRepository: UserRepository ,
    hashingService: HashingService
){
    post("/login") {

        try {

            val loginBody = call.receive<LoginBody>()

            val user = userRepository.loginUser(loginBody.email , loginBody.password)

            if(user == null){
                call.respond(HttpStatusCode.Unauthorized , "The user email provided does not exist.")
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


        }catch (e : Exception){
            call.respond(HttpStatusCode.InternalServerError , message = e.localizedMessage)
        }

    }

    post("/signup"){

        try {
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

        }catch (e : Exception){
            call.respond(HttpStatusCode.InternalServerError , message = e.localizedMessage)
        }

    }

    post("/refresh_token") {

        try {
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

        }catch (e : Exception){
            call.respond(HttpStatusCode.InternalServerError , message = e.localizedMessage)
        }

    }

}