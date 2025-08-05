package com.github.ckgod.markdownconverter.toolWindow

import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.EditorTextField
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBScrollPane
import java.awt.FlowLayout
import javax.swing.*
import javax.swing.plaf.basic.BasicSplitPaneUI

class MarkdownConverterToolWindow(toolWindow: ToolWindow) {
    private val project: Project = toolWindow.project

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