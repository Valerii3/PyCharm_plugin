package com.valerii.ov.pycharm_plugin.dev

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import com.valerii.ov.pycharm_plugin.ui.DemoPanelFactory
import org.json.JSONException

fun sendCodeToOpenAI(code: String, apiKey: String) {
    val apiUrl = "https://api.openai.com/v1/chat/completions"
    val messages = listOf(mapOf("role" to "user", "content" to "Explain the following Python function and its purpose:\n\n$code"))

    val client = OkHttpClient()

    val mediaType = "application/json; charset=utf-8".toMediaType()
    val requestBody = JSONObject()
        .put("model", "gpt-3.5-turbo")
        .put("messages", messages)
        .put("temperature", 0.7) // Adjust as needed
        .toString()
        .toRequestBody(mediaType)

    val request = Request.Builder()
        .url(apiUrl)
        .post(requestBody)
        .addHeader("Authorization", "Bearer $apiKey")
        .addHeader("Content-Type", "application/json")
        .build()

    val toolWindowFactory = DemoPanelFactory.getInstance()

    client.newCall(request).execute().use { response ->
        if (response.isSuccessful) {
            val responseBody = JSONObject(response.body!!.string())
            val answer = try {
                // Corrected parsing logic
                responseBody.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
            } catch (e: JSONException) {
                "Failed to parse response: ${responseBody.toString(4)}"
            }
            toolWindowFactory?.updateTextArea(answer)
            answer
        } else {
            val errorMessage = "Error: ${response.code} - ${response.message}"
            println(errorMessage)
            println("Response body: ${response.body?.string()}")
            toolWindowFactory?.updateTextArea(errorMessage)
            errorMessage
        }
    }

}
