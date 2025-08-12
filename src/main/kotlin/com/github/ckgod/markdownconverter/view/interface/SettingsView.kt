package com.github.ckgod.markdownconverter.view.`interface`

interface SettingsView {
    fun showValidationLoading(isLoading: Boolean)
    fun showValidationError(message: String)
    fun showValidationSuccess(message: String)
}