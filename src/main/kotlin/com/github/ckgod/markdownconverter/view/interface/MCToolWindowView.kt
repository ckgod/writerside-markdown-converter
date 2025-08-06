package com.github.ckgod.markdownconverter.view.`interface`

interface MCToolWindowView {
    fun showLoading(isLoading: Boolean)
    fun showResult(result: String)
    fun showApiKeyError(message: String)
    fun showApiKeyValidationLoading(isLoading: Boolean)
    fun switchToMainPanel()
}