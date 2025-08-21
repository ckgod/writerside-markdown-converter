package com.github.ckgod.markdownconverter.model.types

sealed class Language(
    val displayName: String,
    val targetLanguageForPrompt: String,
    val translationModeForPrompt: String = "TRANSLATE_AND_FORMAT"
) {
    override fun toString(): String = displayName

    object FormatOnly : Language("Format Only (No Translation)", "N/A", "FORMAT_ONLY")
    object English : Language("English", "English")
    object Korean : Language("한국어", "Korean")
    object Japanese : Language("日本語", "Japanese")
    object ChineseSimplified : Language("简体中文", "Chinese Simplified")
    object Spanish : Language("Español", "Spanish")
    object French : Language("Français", "French")
    object German : Language("Deutsch", "German")
    object Russian : Language("Русский", "Russian")
    object Portuguese : Language("Português", "Portuguese")
    object Italian : Language("Italiano", "Italian")

    companion object {
        fun all(): Array<Language> {
            val desiredOrder = listOf(
                FormatOnly,
                English,
                Korean,
                ChineseSimplified,
                Japanese
            )

            val allLanguages = Language::class.sealedSubclasses
                .mapNotNull { it.objectInstance }

            return allLanguages.sortedWith(
                compareBy { language ->
                    val index = desiredOrder.indexOf(language)
                    if (index == -1) Int.MAX_VALUE else index
                }
            ).toTypedArray()
        }
    }
}