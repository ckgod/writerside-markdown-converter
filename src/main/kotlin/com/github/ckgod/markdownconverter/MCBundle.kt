package com.github.ckgod.markdownconverter

import com.intellij.DynamicBundle
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.PropertyKey

@NonNls
private const val BUNDLE = "messages.MarkdownConverterBundle"

/**
 * 변환기 플러그인에서 사용되는 지역화된 문자열(localized strings)을 제공합니다.
 *
 * 이 객체는 `messages/MarkdownConverterBundle.properties` 파일에 정의된 키-값 쌍에 접근하는 역할을 합니다.
 * 이를 통해 UI 텍스트를 코드와 분리하여 관리하고, 다국어 지원을 용이하게 합니다.
 *
 * @see DynamicBundle
 */
object MCBundle : DynamicBundle(BUNDLE) {

    /**
     * 지정된 키에 해당하는 메시지를 반환합니다.
     *
     * @param key `messages.MarkdownConverterBundle.properties` 파일에 정의된 프로퍼티 키
     * @param params 메시지 내의 플레이스홀더({0}, {1}, ...)에 채워질 값들
     * @return 지역화된 메시지 문자열
     */
    @JvmStatic
    fun message(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any) =
        getMessage(key, *params)

    @Suppress("unused")
    @JvmStatic
    fun messagePointer(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any) =
        getLazyMessage(key, *params)
}
