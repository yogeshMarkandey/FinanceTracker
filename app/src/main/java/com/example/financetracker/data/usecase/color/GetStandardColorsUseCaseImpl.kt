package com.example.financetracker.data.usecase.color

import android.content.Context
import com.example.financetracker.domain.model.local.StandardColor
import com.example.financetracker.domain.usecase.color.GetStandardColorsUseCase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class GetStandardColorsUseCaseImpl @Inject constructor(private val context: Context) :
    GetStandardColorsUseCase {
    override fun execute(): List<StandardColor> {
        val json = readJsonFromAssets(context, "standard_colors.json")
        val list = convertJsonToStandardColorList(json)

        return list
    }

    private fun readJsonFromAssets(context: Context, fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }

    private fun convertJsonToStandardColorList(json: String): List<StandardColor> {
        val gson = Gson()
        val listType = object : TypeToken<List<StandardColor>>() {}.type
        return gson.fromJson(json, listType)
    }

}