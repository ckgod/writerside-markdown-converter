package com.github.ckgod.markdownconverter.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.github.ckgod.markdownconverter.MarkdownConverterBundle

/**
 * 마크다운 변환과 관련된 프로젝트 수준의 서비스입니다.
 *
 * 이 서비스는 특정 프로젝트의 라이프사이클 동안 유지되며, 프로젝트별 설정, 데이터 캐싱,
 * 또는 핵심 비즈니스 로직(예: Gemini API 호출)을 처리하는 데 사용될 수 있습니다.
 * `ServiceManager`를 통해 인스턴스를 얻을 수 있습니다.
 *
 * @param project 이 서비스가 종속된 현재 프로젝트
 */
@Service(Service.Level.PROJECT)
class MarkdownConverterService(project: Project) {

    init {
        thisLogger().info(MarkdownConverterBundle.message("projectService", project.name))
        thisLogger().warn("Don't forget to remove all non-needed sample code files with their corresponding registration entries in `plugin.xml`.")
    }

    fun getRandomNumber() = (1..100).random()
}
