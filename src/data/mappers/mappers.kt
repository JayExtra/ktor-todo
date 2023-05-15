package com.dev.james.data.mappers

import com.dev.james.data.entities.TodoEntity
import com.dev.james.data.entities.UserEntity
import com.dev.james.domain.repository.UserRepository
import com.dev.james.domain.models.ToDo


fun TodoEntity.mapToToDoDomainObject(): ToDo {
    return ToDo(
        id = id,
        title = title,
        done = done,
        user_id = user_id
    )
}

fun UserEntity.mapToUserDomainObject() : UserRepository.User {

    return UserRepository.User(
        userId = user_id ,
        email = email ,
        username = username ,
        salt = salt ,
        password = password ,
        refreshToken = refresh_token
    )

}