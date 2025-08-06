package com.github.ckgod.markdownconverter.presenter

import com.github.ckgod.markdownconverter.MCBundle
import com.github.ckgod.markdownconverter.model.services.ApiKeyService
import com.github.ckgod.markdownconverter.model.services.GeminiApiService
import com.github.ckgod.markdownconverter.view.`interface`.MCToolWindowView
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MCToolWindowPresenter(
    private val view: MCToolWindowView,
    private val project: Project
) {
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
                val result = withContext(Dispatchers.IO) {
                    geminiApiService.convert(inputText)
                }
                view.showResult(result)
            } catch (e: Exception) {
                view.showResult(e.localizedMessage)
            } finally {
                view.showLoading(false)
            }
        }
    }
}