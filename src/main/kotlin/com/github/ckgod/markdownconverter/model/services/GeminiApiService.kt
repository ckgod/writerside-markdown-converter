package com.github.ckgod.markdownconverter.model.services

import com.github.ckgod.markdownconverter.MCBundle
import com.google.genai.Client
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project

@Service(Service.Level.PROJECT)
class GeminiApiService(private val project: Project) {

    suspend fun convert(inputText: String): String {
        val apiKey = service<ApiKeyService>().getApiKey()

        if (apiKey.isNullOrBlank()) {
            return MCBundle.message("errorKey")
        }

        val client = Client.builder()
            .apiKey(apiKey).build()

        return client.models.generateContent(
            "gemini-2.5-flash",
            inputText,
            null
        ).text() ?: MCBundle.message("errorNoContent")
    }
}