package com.github.ckgod.markdownconverter.services

import com.github.ckgod.markdownconverter.task.runBackgroundTask
import com.google.genai.Client
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import org.jetbrains.kotlin.idea.gradleTooling.get

@Service(Service.Level.PROJECT)
class GeminiApiService(private val project: Project) {
    suspend fun generateDocumentation(inputText: String, onLoading: () -> Unit, onResult: (String) -> Unit) {
        val apiKey = service<ApiKeyService>().getApiKey()

        if (apiKey.isNullOrBlank()) {
            onResult.invoke("Invalid API Key")
            return
        }

        runBackgroundTask(project, inputText, "Generate documentation") {
            backgroundTask { text, indicator ->
                onLoading.invoke()
                indicator.text = "Gemini 클라이언트 초기화 중..."

                val client = Client.builder()
                    .apiKey(apiKey).build()

                indicator.text = "프롬프트 구성 및 요청 전송 중..."

                client.models.generateContent(
                    "gemini-2.5-flash",
                    inputText,
                    null
                ).text()
            }

            onSuccess { result ->
                onResult.invoke(result ?: "")
            }

            onFailure { e ->
                onResult.invoke(e.localizedMessage)
            }

            onCancel {
                onResult.invoke("작업이 취소 되었습니다.")
            }
        }
    }
}