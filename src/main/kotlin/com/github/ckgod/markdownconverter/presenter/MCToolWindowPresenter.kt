package com.github.ckgod.markdownconverter.presenter

import com.github.ckgod.markdownconverter.MCBundle
import com.github.ckgod.markdownconverter.model.services.ApiKeyService
import com.github.ckgod.markdownconverter.model.services.GeminiApiService
import com.github.ckgod.markdownconverter.model.types.Language
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
        private const val REPLACE_INPUT = "[INPUT]"
        private const val REPLACE_MODE = "[TRANS_MODE]"
        private const val REPLACE_LANG = "[TRANS_LANG]"
    }
    private val geminiApiService = service<GeminiApiService>()
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

    fun onConvertClicked(inputText: String, language: Language = Language.FormatOnly, semanticMarkup: Boolean = false) {
        if (inputText.isBlank()) {
            view.showResult(MCBundle.message("errorNoInput"))
            return
        }

        presenterScope.launch {
            view.showLoading(true)
            try {
                val prompt = loadPrompt(semanticMarkup)
                    .replace(REPLACE_INPUT, inputText)
                    .replace(REPLACE_MODE, language.translationModeForPrompt)
                    .replace(REPLACE_LANG, language.targetLanguageForPrompt)

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

    private suspend fun loadPrompt(semanticMarkup: Boolean): String = withContext(Dispatchers.IO) {
        val pluginId = PluginId.getId("com.github.ckgod.markdownconverter")
        val pluginClassLoader = PluginManagerCore.getPlugin(pluginId)?.pluginClassLoader
            ?: throw IllegalStateException("No plugin loader found for $pluginId")

        val processingRulesFile = if (semanticMarkup) {
            "prompt_processing_rules_advanced.txt"
        } else {
            "prompt_processing_rules_basic.txt"
        }

        val promptFiles = listOf(
            "prompt_instruction_header.txt",
            "prompt_persona.txt",
            "prompt_task_directive.txt",
            processingRulesFile,
            "prompt_security_output_control.txt",
            "prompt_instruction_footer.txt"
        )

        promptFiles.joinToString("\n\n") { fileName ->
            val inputStream = pluginClassLoader.getResourceAsStream("prompts/$fileName")
                ?: throw IllegalStateException("Resource file not found: prompts/$fileName")
            inputStream.bufferedReader(Charsets.UTF_8).readText()
        }
    }
}