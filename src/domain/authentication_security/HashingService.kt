package com.dev.james.domain.authentication_security

import com.dev.james.domain.models.SaltedHash

interface HashingService {
    fun generateSlatedHash(value : String , saltLength : Int = 32) : SaltedHash
    fun verify(value : String , saltedHash : SaltedHash) : Boolean
}