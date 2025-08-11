package com.github.ckgod.markdownconverter.presenter

import com.github.ckgod.markdownconverter.MCBundle
import com.github.ckgod.markdownconverter.model.services.ApiKeyService
import com.github.ckgod.markdownconverter.model.services.GeminiApiService
import com.github.ckgod.markdownconverter.view.`interface`.MCToolWindowView
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.components.service
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MCToolWindowPresenter(
    private val view: MCToolWindowView,
    project: Project
) {
    companion object {
        private const val REPLACE_TEXT = "[CKGOD]"
    }
    private val geminiApiService = project.service<GeminiApiService>()
    private val apiKeyService = service<ApiKeyService>()

    private val presenterScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    fun onStartClicked(apiKey: String) {
        if (apiKey.isBlank()) {
            view.showApiKeyError(MCBundle.message("errorKey"))
            return
        }
        view.showApiKeyError("")

        presenterScope.launch {
            view.showApiKeyValidationLoading(true)
            try {
                geminiApiService.validateApiKey(apiKey)
                apiKeyService.saveApiKey(apiKey)

                view.switchToMainPanel()
            } catch (e: Exception) {
                view.showApiKeyError(e.localizedMessage)
            } finally {
                view.showApiKeyValidationLoading(false)
            }
        }
    }

    fun onConvertClicked(inputText: String) {
        if (inputText.isBlank()) {
            view.showResult(MCBundle.message("errorNoInput"))
            return
        }

        presenterScope.launch {
            view.showLoading(true)
            try {
                val prompt = loadPrompt().replace(REPLACE_TEXT, inputText)
                val result = geminiApiService.convert(prompt)
                view.showResult(result)
            } catch (e: Exception) {
                e.printStackTrace()
                view.showResult(e.localizedMessage)
            } finally {
                view.showLoading(false)
            }
        }
    }

    private suspend fun loadPrompt(): String = withContext(Dispatchers.IO) {
        val pluginId = PluginId.getId("com.github.ckgod.markdownconverter")
        val pluginClassLoader = PluginManagerCore.getPlugin(pluginId)?.pluginClassLoader
            ?: throw IllegalStateException("No plugin loader found for $pluginId")
        val inputStream = pluginClassLoader.getResourceAsStream("prompts/converter_prompt.txt")
            ?: throw IllegalStateException("Resource file not found: prompts/converter_prompt.txt")

        inputStream.bufferedReader(Charsets.UTF_8).readText()
    }
}