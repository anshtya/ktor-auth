package com.anshtya.jetx.server.repository

import com.anshtya.jetx.server.model.User

class UserRepository {
    private val userList = mutableListOf<User>()

    fun getUser(email: String): User? =
        userList.find { it.email == email }

    fun saveUser(user: User) {
        userList.add(user)
    }
}