package com.github.ckgod.markdownconverter.presenter

import com.github.ckgod.markdownconverter.MCBundle
import com.github.ckgod.markdownconverter.model.services.ApiKeyService
import com.github.ckgod.markdownconverter.model.services.GeminiApiService
import com.github.ckgod.markdownconverter.view.`interface`.SettingsView
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.service
import kotlinx.coroutines.*

class SettingsPresenter(private val view: SettingsView) : Disposable {
    private val geminiApiService = service<GeminiApiService>()
    private val apiKeyService = service<ApiKeyService>()

    private val presenterScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun onConnectedClick(apiKey: String) {
        if (apiKey.isBlank()) {
            view.showValidationError(MCBundle.message("errorKey"))
            return
        }
        view.showValidationError("")

        presenterScope.launch {
            view.showValidationLoading(true)
            try {
                geminiApiService.validateApiKey(apiKey)

                view.showValidationSuccess(MCBundle.message("apiConnectionSuccess"))
            } catch (e: Exception) {
                view.showValidationError(e.localizedMessage)
            } finally {
                view.showValidationLoading(false)
            }
        }
    }

    fun onApplyClicked(apiKey: String, onSuccess: (() -> Unit)? = null) = presenterScope.launch {
        apiKeyService.saveApiKey(apiKey, onSuccess)
    }

    override fun dispose() {
        presenterScope.cancel()
    }
}