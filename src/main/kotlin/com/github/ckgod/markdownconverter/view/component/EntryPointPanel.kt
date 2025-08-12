package com.github.ckgod.markdownconverter.view.component

import com.github.ckgod.markdownconverter.MCBundle
import com.intellij.ui.dsl.builder.panel
import javax.swing.JButton
import javax.swing.JPanel

class EntryPointPanel : JPanel() {
    private val apiKeyPanel = ApiKeyCertificationPanel()
    val apiKeyField = apiKeyPanel.apiKeyField
    val saveButton = JButton(MCBundle.message("start"))

    init {
        val ui = panel {
            row {
                cell(apiKeyPanel)
            }
            row {
                cell(saveButton)
            }
        }
        add(ui)
    }

    fun showApiKeyError(message: String) {
        apiKeyPanel.showApiKeyError(message)
    }

    fun showLoading(isLoading: Boolean) {
        apiKeyPanel.showLoading(isLoading)
        saveButton.isEnabled = !isLoading
    }

}