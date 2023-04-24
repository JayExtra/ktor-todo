package com.dev.james.data.authentication.security

interface HashingService {
    fun generateSlatedHash(value : String , saltLength : Int = 32) : SaltedHash
    fun verify(value : String , saltedHash : SaltedHash) : Boolean
}