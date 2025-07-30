package com.github.ckgod.markdownconverter.toolWindow

import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.content.ContentFactory
import com.github.ckgod.markdownconverter.MarkdownConverterBundle
import com.github.ckgod.markdownconverter.services.MarkdownConverterService
import javax.swing.JButton

/**
 * 마크다운 변환기 플러그인의 메인 UI인 Tool Window를 생성합니다.
 *
 * 이 클래스는 `plugin.xml` 파일의 `<toolWindow>` 확장점에 등록되어야 하며,
 * ID를 통해 인텔리제이 플랫폼이 인식하고 Tool Window를 초기화할 때 `createToolWindowContent` 메소드를 호출합니다.
 *
 * @see ToolWindowFactory
 */
class MarkdownConverterToolWindowFactory : ToolWindowFactory {

    init {
        thisLogger().warn("Don't forget to remove all non-needed sample code files with their corresponding registration entries in `plugin.xml`.")
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val myToolWindow = MyToolWindow(toolWindow)
        val content = ContentFactory.getInstance().createContent(myToolWindow.getContent(), null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

    class MyToolWindow(toolWindow: ToolWindow) {

        private val service = toolWindow.project.service<MarkdownConverterService>()

        fun getContent() = JBPanel<JBPanel<*>>().apply {
            val label = JBLabel(MarkdownConverterBundle.message("randomLabel", "?"))

            add(label)
            add(JButton(MarkdownConverterBundle.message("shuffle")).apply {
                addActionListener {
                    label.text = MarkdownConverterBundle.message("randomLabel", service.getRandomNumber())
                }
            })
        }
    }
}
