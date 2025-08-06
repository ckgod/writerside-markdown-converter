package com.github.ckgod.markdownconverter.presenter

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

    private val presenterScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    fun onConvertClicked() {
        val inputText = view.getInputText()
        if (inputText.isBlank()) {
            view.showResult("텍스트를 입력하세요.")
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