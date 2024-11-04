package com.anshtya.jetx.server.util

object ErrorMessage {
    const val INTERNAL_ERROR = "Server error occurred."
    const val INCORRECT_CREDENTIALS = "Credentials are incorrect."
    const val INVALID_EXPIRED_TOKEN = "Token is not valid or has expired."
    const val USER_ALREADY_EXISTS = "User already exists. Please sign-in instead."
    const val USER_NOT_FOUND = "User not found. Please sign-up instead."
}