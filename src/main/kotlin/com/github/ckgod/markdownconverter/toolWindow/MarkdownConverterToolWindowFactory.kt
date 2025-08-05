package com.github.ckgod.markdownconverter.toolWindow

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory

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
        val myToolWindow = MarkdownConverterToolWindow(toolWindow)
        val content = ContentFactory.getInstance().createContent(myToolWindow.getContents(), null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true
}
