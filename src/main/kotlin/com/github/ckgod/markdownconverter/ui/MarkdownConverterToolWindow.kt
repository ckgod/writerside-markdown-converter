package com.github.ckgod.markdownconverter.ui

import com.github.ckgod.markdownconverter.services.GeminiApiService
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.EditorTextField
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.ContentFactory
import java.awt.FlowLayout
import javax.swing.*
import javax.swing.plaf.basic.BasicSplitPaneUI

class MarkdownConverterToolWindow(toolWindow: ToolWindow) {
    private val project: Project = toolWindow.project
    private val geminiService = project.service<GeminiApiService>()

    private val factory = EditorFactory.getInstance()
    private val inputDocument = factory.createDocument("")
    private val outputDocument = factory.createDocument("")
    private val fileType = FileTypeManager.getInstance().findFileTypeByName("Markdown")

    private val inputField = EditorTextField(inputDocument, project, fileType).apply {
        setOneLineMode(false)
        isViewer = false
        setEditorStyle()
    }
    private val outputField = EditorTextField(outputDocument, project, fileType).apply {
        setOneLineMode(false)
        isViewer = true
        setEditorStyle()
    }
    private val convertButton = JButton("Convert")

    private fun setupActions() {
        convertButton.addActionListener {
//
//            geminiService.generateDocumentation(inputField.text) {
//                outputField.setText(it)
//            }
        }
    }

    fun getContents(): JComponent {
        setupActions()

        fun createPanel(field: JComponent, label: String? = null): JComponent {
            val panel = JBPanel<JBPanel<*>>().apply { layout = BoxLayout(this, BoxLayout.Y_AXIS) }
            val scrollPane = JBScrollPane(field)
            label?.let {
                panel.add(JBLabel(it))
            }
            panel.add(Box.createVerticalStrut(5))
            panel.add(scrollPane)

            return panel
        }

        val inputPanel = createPanel(inputField, "Text to Convert")
        val outputPanel = createPanel(outputField, "Result: ")
        val buttonPanel = JBPanel<JBPanel<*>>().apply {
            layout = FlowLayout(FlowLayout.CENTER)
            add(convertButton)
        }

        val bottomSplit = JSplitPane(JSplitPane.VERTICAL_SPLIT).apply {
            topComponent = buttonPanel
            bottomComponent = outputPanel
            resizeWeight = 0.07692308
        }
        (bottomSplit.ui as? BasicSplitPaneUI)?.apply {
            divider?.isEnabled = false
            bottomSplit.dividerSize = 0
        }
        val mainSplit = JSplitPane(JSplitPane.VERTICAL_SPLIT).apply {
            topComponent = inputPanel
            bottomComponent = bottomSplit
            resizeWeight = 0.48
        }
        (mainSplit.ui as? BasicSplitPaneUI)?.apply {
            divider?.isEnabled = false
            mainSplit.dividerSize = 0
        }

        return mainSplit
    }

    fun EditorTextField.setEditorStyle() {
        addSettingsProvider { editorEx ->
            val globalScheme = EditorColorsManager.getInstance().globalScheme
            val localScheme = globalScheme.clone() as EditorColorsScheme
            editorEx.colorsScheme = localScheme
            editorEx.backgroundColor = localScheme.defaultBackground
            editorEx.settings.apply {
                isLineNumbersShown = true
                isFoldingOutlineShown = true
                isRightMarginShown = true
                isUseSoftWraps = true
            }
        }
    }
}

/**
 * 마크다운 변환기 플러그인의 메인 UI인 Tool Window를 생성합니다.
 *
 * 이 클래스는 `plugin.xml` 파일의 `<toolWindow>` 확장점에 등록되어야 하며,
 * ID를 통해 인텔리제이 플랫폼이 인식하고 Tool Window를 초기화할 때 `createToolWindowContent` 메소드를 호출합니다.
 *
 * @see com.intellij.openapi.wm.ToolWindowFactory
 */
class MarkdownConverterToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val myToolWindow = MarkdownConverterToolWindow(toolWindow)
        val content = ContentFactory.getInstance().createContent(myToolWindow.getContents(), null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true
}