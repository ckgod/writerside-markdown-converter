package com.github.ckgod.markdownconverter.action

import com.github.ckgod.markdownconverter.configure.SettingsConfigurable
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project

class OpenSettingsAction(private val project: Project) : AnAction(
    "Settings",
    "Open K-Doc Converter settings",
    AllIcons.General.GearPlain
) {
    override fun actionPerformed(p0: AnActionEvent) {
        ShowSettingsUtil.getInstance().showSettingsDialog(
            project,
            SettingsConfigurable::class.java
        )
    }
}