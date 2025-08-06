package com.github.ckgod.markdownconverter.view.uiimpl

import com.github.ckgod.markdownconverter.MCBundle
import com.github.ckgod.markdownconverter.presenter.MCToolWindowPresenter
import com.github.ckgod.markdownconverter.view.`interface`.MCToolWindowView
import com.github.ckgod.markdownconverter.view.utils.setEditorStyle
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.EditorTextField
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.ContentFactory
import com.intellij.util.ui.AsyncProcessIcon
import java.awt.BorderLayout
import java.awt.CardLayout
import java.awt.FlowLayout
import java.awt.GridBagLayout
import javax.swing.*
import javax.swing.plaf.basic.BasicSplitPaneUI

class MCToolWindow(toolWindow: ToolWindow): MCToolWindowView {
    companion object {
        private const val RESULT_CARD = "RESULT"
        private const val LOADING_CARD = "LOADING"
    }
    private val project: Project = toolWindow.project
    private val presenter = MCToolWindowPresenter(this, project)

    private val inputField: EditorTextField
    private val outputField: EditorTextField

    private val convertButton = JButton(MCBundle.message("convert"))
    private val loadingIcon = AsyncProcessIcon(MCBundle.message("generate"))

    private val resultCardLayout = CardLayout()
    private val resultPanel = JPanel(resultCardLayout)
    private val mainPanel: JComponent

    init {
        val fileType = FileTypeManager.getInstance().findFileTypeByName("Markdown")
        inputField = createEditorTextField(project, fileType, isViewer = false)
        outputField = createEditorTextField(project, fileType, isViewer = true)

        setupActions()
        mainPanel = createMainPanel()
    }

    private fun setupActions() {
        convertButton.addActionListener {
            presenter.onConvertClicked()
        }
    }

    fun getContents(): JComponent = mainPanel

    private fun createMainPanel(): JComponent {
        val inputPanel = createEditorPanel(inputField, MCBundle.message("infoTextPlaceholder"))
        val controlPanel = createControlPanel()
        val resultContainerPanel = createResultPanel()

        val bottomSplit = JSplitPane(JSplitPane.VERTICAL_SPLIT, controlPanel, resultContainerPanel)
            .configureDefaults(resizeWeight = 0.0)

        return JSplitPane(JSplitPane.VERTICAL_SPLIT, inputPanel, bottomSplit)
            .configureDefaults(resizeWeight = 0.5)
    }

    private fun createEditorPanel(editorField: EditorTextField, labelText: String? = null): JComponent {
        return JBPanel<JBPanel<*>>(BorderLayout(0, 5)).apply {
            labelText?.let { add(JBLabel(labelText), BorderLayout.NORTH) }
            add(JBScrollPane(editorField), BorderLayout.CENTER)
        }
    }

    private fun createControlPanel(): JComponent {
        return JPanel(FlowLayout(FlowLayout.CENTER)).apply {
            add(convertButton)
        }
    }

    private fun createResultPanel(): JComponent {
        val editorCard = JPanel(BorderLayout()).apply {
            add(JBScrollPane(outputField), BorderLayout.CENTER)
        }
        val loadingCard = JPanel(GridBagLayout()).apply {
            add(loadingIcon)
        }
        return resultPanel.apply {
            add(editorCard, RESULT_CARD)
            add(loadingCard, LOADING_CARD)
        }
    }

    private fun createEditorTextField(project: Project, fileType: FileType?, isViewer: Boolean): EditorTextField {
        val document = EditorFactory.getInstance().createDocument("")
        return EditorTextField(document, project, fileType).apply {
            this.isViewer = isViewer
            setOneLineMode(false)
            setEditorStyle()
        }
    }

    private fun JSplitPane.configureDefaults(resizeWeight: Double): JSplitPane {
        this.resizeWeight = resizeWeight
        this.dividerSize = 0
        (this.ui as? BasicSplitPaneUI)?.divider?.isEnabled = false
        return this
    }

    override fun getInputText(): String = inputField.text

    override fun showLoading(isLoading: Boolean) {
        convertButton.isEnabled = !isLoading
        inputField.isEnabled = !isLoading

        val cardToShow = if (isLoading) LOADING_CARD else RESULT_CARD
        resultCardLayout.show(resultPanel, cardToShow)
    }

    override fun showResult(result: String) {
        outputField.setText(result)
    }
}

/**
 * 마크다운 변환기 플러그인의 메인 UI인 Tool Window를 생성합니다.
 *
 * 이 클래스는 `plugin.xml` 파일의 `<toolWindow>` 확장점에 등록되어야 하며,
 * ID를 통해 인텔리제이 플랫폼이 인식하고 Tool Window를 초기화할 때 `createToolWindowContent` 메소드를 호출합니다.
 *
 * @see ToolWindowFactory
 */
class MarkdownConverterToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val mcToolWindow = MCToolWindow(toolWindow)
        val content = ContentFactory.getInstance().createContent(mcToolWindow.getContents(), null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true
}