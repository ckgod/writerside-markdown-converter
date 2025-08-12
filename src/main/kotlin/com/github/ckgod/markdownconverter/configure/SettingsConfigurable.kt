package com.github.ckgod.markdownconverter.configure

import com.github.ckgod.markdownconverter.MCBundle
import com.github.ckgod.markdownconverter.model.services.ApiKeyService
import com.github.ckgod.markdownconverter.presenter.SettingsPresenter
import com.github.ckgod.markdownconverter.view.component.ApiKeyCertificationPanel
import com.github.ckgod.markdownconverter.view.`interface`.SettingsView
import com.intellij.openapi.components.service
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.util.NlsContexts
import com.intellij.ui.dsl.builder.panel
import javax.swing.JButton
import javax.swing.JComponent

class SettingsConfigurable : Configurable, SettingsView {
    private val settingsPresenter by lazy { SettingsPresenter(this) }
    private val apiKeyService = service<ApiKeyService>()
    private val apiKeyPanel = ApiKeyCertificationPanel()
    private val testButton = JButton(MCBundle.message("apiConnectionTestLabel"))
    private var originApiKey: String = ""

    init {
        testButton.addActionListener {
            println("--- DEBUG: Test Button Clicked! ---")
            settingsPresenter.onConnectedClick(String(apiKeyPanel.apiKeyField.password))
        }
    }

    override fun getDisplayName(): @NlsContexts.ConfigurableName String? = "K-Doc Converter"

    override fun createComponent(): JComponent? {
        reset()
        return panel {
            row {
                cell(apiKeyPanel)
            }
            row {
                cell(testButton)
            }
        }
    }

    override fun reset() {
        originApiKey = apiKeyService.getApiKey() ?: ""
        apiKeyPanel.apiKeyField.text = apiKeyService.getApiKey() ?: ""
    }

    override fun isModified(): Boolean {
        return String(apiKeyPanel.apiKeyField.password) != originApiKey
    }

    override fun apply() {
        val newApiKey = String(apiKeyPanel.apiKeyField.password)
        settingsPresenter.onApplyClicked(newApiKey) {
            originApiKey = newApiKey
        }
    }

    override fun showValidationLoading(isLoading: Boolean) {
        apiKeyPanel.showLoading(isLoading)
    }

    override fun showValidationError(message: String) {
        apiKeyPanel.showApiKeyError(message)
    }

    override fun showValidationSuccess(message: String) {
        apiKeyPanel.showApiKeySuccess(message)
    }

    override fun disposeUIResources() {
        super.disposeUIResources()
        settingsPresenter.dispose()
    }
}