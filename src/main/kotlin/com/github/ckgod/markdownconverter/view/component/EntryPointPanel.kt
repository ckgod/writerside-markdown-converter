package com.github.ckgod.markdownconverter.view.component

import com.github.ckgod.markdownconverter.MCBundle
import com.intellij.ui.components.JBPasswordField
import com.intellij.ui.dsl.builder.Align
import com.intellij.ui.dsl.builder.panel
import javax.swing.JButton
import javax.swing.JPanel

class EntryPointPanel: JPanel() {
    val apiKeyField = JBPasswordField()
    val saveButton = JButton(MCBundle.message("start"))

    init {
        val ui = panel {
            row {
                label(MCBundle.message("apiKeyInfo"))
            }
            row(MCBundle.message("apiKeyLabel")) {
                cell(apiKeyField)
                    .resizableColumn()
                    .align(Align.FILL)
            }
            row {
                cell(saveButton)
            }
        }
        add(ui)
    }
}