package com.example.financetracker.data.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T> Gson.fromJson(json: String) =
    fromJson<T>(json, object : TypeToken<T>() {}.type)

class CustomException(
    val errorMessage: String = "Something When Wrong!",
    val code: Int? = null,
) : Exception()