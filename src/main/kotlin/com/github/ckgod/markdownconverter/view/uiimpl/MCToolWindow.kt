package com.github.ckgod.markdownconverter.view.uiimpl

import com.github.ckgod.markdownconverter.presenter.MCToolWindowPresenter
import com.github.ckgod.markdownconverter.view.component.ConverterPanel
import com.github.ckgod.markdownconverter.view.component.EntryPointPanel
import com.github.ckgod.markdownconverter.view.`interface`.MCToolWindowView
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import java.awt.CardLayout
import javax.swing.*

class MCToolWindow(toolWindow: ToolWindow): MCToolWindowView {
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
    }

    private fun setupActions() {
        converterPanel.convertButton.addActionListener {
            presenter.onConvertClicked(converterPanel.inputEditor.text)
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
        converterPanel.outputEditor.setText(result)
    }
}

class MarkdownConverterToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val mcToolWindow = MCToolWindow(toolWindow)
        val content = ContentFactory.getInstance().createContent(mcToolWindow.getContents(), null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true
}