package com.github.ckgod.markdownconverter.view.utils

import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.ui.EditorTextField

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