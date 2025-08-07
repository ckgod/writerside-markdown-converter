package com.github.ckgod.markdownconverter.model.services

import com.github.ckgod.markdownconverter.MCBundle
import com.google.genai.Client
import com.google.genai.types.ListModelsConfig
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Service(Service.Level.PROJECT)
class GeminiApiService(private val project: Project) {

    suspend fun convert(inputText: String): String = withContext(Dispatchers.IO) {
        val apiKey = service<ApiKeyService>().getApiKey()

        if (apiKey.isNullOrBlank()) {
            MCBundle.message("errorKey")
        }

        val client = Client.builder()
            .apiKey(apiKey).build()

        client.models.generateContent(
            "gemini-2.5-flash",
            inputText,
            null
        ).text() ?: MCBundle.message("errorNoContent")
    }

    suspend fun validateApiKey(apiKey: String) = withContext(Dispatchers.IO) {
        val client = Client.builder().apiKey(apiKey).build()
        client.models.list(ListModelsConfig.builder().build())
    }
}