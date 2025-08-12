package com.github.ckgod.markdownconverter.view.component

import com.github.ckgod.markdownconverter.MCBundle
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPasswordField
import com.intellij.ui.dsl.builder.Align
import com.intellij.ui.dsl.builder.panel
import com.intellij.util.ui.AsyncProcessIcon
import javax.swing.JPanel

class ApiKeyCertificationPanel : JPanel() {
    val apiKeyField = JBPasswordField()

    private val errorLabel = JBLabel().apply {
        foreground = JBColor.RED
        isVisible = false
    }
    private val loadingIcon = AsyncProcessIcon(MCBundle.message("generate")).apply {
        isVisible = false
    }

    init {
        val ui = panel {
            row {
                label(MCBundle.message("apiKeyInfo"))
            }
            row {
                browserLink(
                    text = MCBundle.message("apiKeyCreateDescription"),
                    url = MCBundle.message("apiKeyLink")
                )
            }
            row(MCBundle.message("apiKeyLabel")) {
                cell(apiKeyField)
                    .resizableColumn()
                    .align(Align.FILL)
                cell(loadingIcon)
            }
            row {
                cell(errorLabel)
            }
        }
        add(ui)
    }

    fun showApiKeyError(message: String) {
        errorLabel.foreground = JBColor.RED
        errorLabel.text = message
        errorLabel.isVisible = message.isNotBlank()
    }

    fun showApiKeySuccess(message: String) {
        errorLabel.foreground = JBColor.BLUE
        errorLabel.text = message
        errorLabel.isVisible = message.isNotBlank()
    }

    fun showLoading(isLoading: Boolean) {
        loadingIcon.isVisible = isLoading
        apiKeyField.isEnabled = !isLoading

        parent?.revalidate()
        parent?.repaint()
    }
}