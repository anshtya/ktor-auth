package com.anshtya.jetx.server.repository

import com.anshtya.jetx.server.model.User

class UserRepository {
    private val userList = mutableListOf<User>()

    fun getUser(username: String): User? =
        userList.find { it.username == username }

    fun saveUser(user: User) {
        userList.add(user)
    }
}