package com.github.ckgod.markdownconverter.view.component

import com.github.ckgod.markdownconverter.MCBundle
import com.github.ckgod.markdownconverter.model.types.Language
import com.github.ckgod.markdownconverter.view.utils.setEditorStyle
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.EditorTextField
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.ui.AsyncProcessIcon
import java.awt.BorderLayout
import java.awt.CardLayout
import java.awt.FlowLayout
import java.awt.GridBagLayout
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JSplitPane
import javax.swing.plaf.basic.BasicSplitPaneUI

class ConverterPanel(project: Project) : JPanel(BorderLayout()) {
    companion object {
        private const val RESULT_CARD = "RESULT"
        private const val LOADING_CARD = "LOADING"
    }

    val inputEditor: EditorTextField
    val outputEditor: EditorTextField
    val convertButton = JButton(MCBundle.message("convert"))
    private val comboBox = ComboBox(Language.all())
    val selectedOption get() = comboBox.selectedItem as Language

    private val clearButton = JButton(MCBundle.message("clear"))

    private val resultCardLayout = CardLayout()
    private val resultPanel = JPanel(resultCardLayout)
    private val loadingIcon = AsyncProcessIcon(MCBundle.message("generate"))

    init {
        val fileType = FileTypeManager.getInstance().findFileTypeByName("Markdown")
        inputEditor = createEditorTextField(project, fileType, isViewer = false)
        outputEditor = createEditorTextField(project, fileType, isViewer = true)

        val inputPanel = createEditorPanel(inputEditor, MCBundle.message("infoTextPlaceholder"))
        val controlPanel = createControlPanel()
        val resultPanel = createResultPanel()

        val bottomSplit = JSplitPane(JSplitPane.VERTICAL_SPLIT, controlPanel, resultPanel)
            .configureDefaults(0.0)
        val mainSplit = JSplitPane(JSplitPane.VERTICAL_SPLIT, inputPanel, bottomSplit)
            .configureDefaults(0.5)

        add(mainSplit, BorderLayout.CENTER)

        clearButton.addActionListener {
            outputEditor.text = ""
            inputEditor.text = ""
        }
    }

    fun showLoading(isLoading: Boolean) {
        convertButton.isEnabled = !isLoading
        inputEditor.isEnabled = !isLoading
        val cardToShow = if (isLoading) LOADING_CARD else RESULT_CARD
        resultCardLayout.show(resultPanel, cardToShow)
    }

    private fun createEditorPanel(editorField: EditorTextField, labelText: String? = null): JComponent {
        return JBPanel<JBPanel<*>>(BorderLayout(0, 5)).apply {
            labelText?.let { add(JBLabel(labelText), BorderLayout.NORTH) }
            add(JBScrollPane(editorField), BorderLayout.CENTER)
        }
    }

    private fun createControlPanel(): JComponent {
        return JPanel(FlowLayout(FlowLayout.CENTER)).apply {
            add(comboBox)
            add(convertButton)
            add(clearButton)
        }
    }

    private fun createResultPanel(): JComponent {
        val editorCard = JPanel(BorderLayout()).apply {
            add(JBScrollPane(outputEditor), BorderLayout.CENTER)
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

}