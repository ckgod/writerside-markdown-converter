package com.github.ckgod.markdownconverter.view.`interface`

interface MCToolWindowView {
    fun getInputText(): String
    fun showLoading(isLoading: Boolean)
    fun showResult(result: String)
}