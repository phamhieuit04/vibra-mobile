package com.example.vibramobile.helpers

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

object JsonHelper {
    val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    fun parseResponse(from: String, to: String): Map<String, JsonElement>? {
        val result = json.parseToJsonElement(from).jsonObject

        if (to != "data" && to != "message" && to != "errors") return null
        val target = result[to] ?: return null

        return when (target) {
            is JsonObject -> target
            is JsonArray -> mapOf(to to target)
            is JsonPrimitive -> mapOf(to to target)
        }
    }

    @OptIn(InternalSerializationApi::class)
    fun <T : Any> parseJson(from: String, to: KClass<T>, key: String? = null): T? {
        val data = parseResponse(from, "data") ?: return null
        val element: JsonElement = if (key == null) {
            JsonObject(data)
        } else {
            data[key] ?: return null
        }
        val serializer = to.serializer()
        return json.decodeFromJsonElement(serializer, element)
    }

    fun parseString(from: String, to: String, key: String): String? {
        return parseResponse(from = from, to = to)?.getValue(key = key)?.jsonPrimitive?.content
    }
}