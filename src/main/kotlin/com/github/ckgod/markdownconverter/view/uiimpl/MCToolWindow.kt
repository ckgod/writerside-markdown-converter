package com.github.ckgod.markdownconverter.view.uiimpl

import com.github.ckgod.markdownconverter.action.OpenSettingsAction
import com.github.ckgod.markdownconverter.model.services.ApiKeyService
import com.github.ckgod.markdownconverter.presenter.MCToolWindowPresenter
import com.github.ckgod.markdownconverter.view.component.ConverterPanel
import com.github.ckgod.markdownconverter.view.component.EntryPointPanel
import com.github.ckgod.markdownconverter.view.`interface`.MCToolWindowView
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.wm.ex.ToolWindowManagerListener
import com.intellij.ui.content.ContentFactory
import java.awt.CardLayout
import javax.swing.*

class MCToolWindow(private val toolWindow: ToolWindow): MCToolWindowView {
    companion object {
        private const val API_KEY_ENTRY_CARD = "API_KEY_ENTRY_CARD"
        private const val CONVERTER_CARD = "CONVERTER_CARD"
    }

    private val project: Project = toolWindow.project
    private val presenter = MCToolWindowPresenter(this, project)

    private val mainCardLayout = CardLayout()
    private val mainPanel = JPanel(mainCardLayout)
    private val entryPointPanel = EntryPointPanel()
    private val converterPanel = ConverterPanel(project)

    init {
        setupActions()

        mainPanel.add(entryPointPanel, API_KEY_ENTRY_CARD)
        mainPanel.add(converterPanel, CONVERTER_CARD)
        checkAndUpdatePanel()
    }

    fun checkAndUpdatePanel() {
        val hasApiKey = !service<ApiKeyService>().getApiKey().isNullOrBlank()
        if (hasApiKey) {
            mainCardLayout.show(mainPanel, CONVERTER_CARD)
        } else {
            mainCardLayout.show(mainPanel, API_KEY_ENTRY_CARD)
        }
    }

    private fun setupActions() {
        converterPanel.convertButton.addActionListener {
            presenter.onConvertClicked(
                converterPanel.inputEditor.text,
                converterPanel.selectedOption,
                converterPanel.isSemanticMarkupEnabled
            )
        }
        entryPointPanel.saveButton.addActionListener {
            presenter.onStartClicked(String(entryPointPanel.apiKeyField.password))
        }
    }

    fun getContents(): JComponent = mainPanel

    override fun showLoading(isLoading: Boolean) {
        converterPanel.showLoading(isLoading)
    }

    override fun showResult(result: String) {
        converterPanel.outputEditor.text = result
    }

    override fun showApiKeyError(message: String) {
        entryPointPanel.showApiKeyError(message)
    }

    override fun showApiKeyValidationLoading(isLoading: Boolean) {
        entryPointPanel.showLoading(isLoading)
    }

    override fun switchToMainPanel() {
        mainCardLayout.show(mainPanel, CONVERTER_CARD)
    }
}

class MarkdownConverterToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val mcToolWindow = MCToolWindow(toolWindow)
        val content = ContentFactory.getInstance().createContent(mcToolWindow.getContents(), null, false)
        toolWindow.contentManager.addContent(content)
        toolWindow.setAdditionalGearActions(DefaultActionGroup(OpenSettingsAction(project)))

        project.messageBus.connect().subscribe(ToolWindowManagerListener.TOPIC, object : ToolWindowManagerListener {
            override fun stateChanged(toolWindowManager: ToolWindowManager) {
                val currentToolWindow = toolWindowManager.getToolWindow("Writerside Markdown Converter")
                if (currentToolWindow?.isVisible == true && currentToolWindow == toolWindow) {
                    mcToolWindow.checkAndUpdatePanel()
                }
            }
        })
    }

    override fun shouldBeAvailable(project: Project) = true
}