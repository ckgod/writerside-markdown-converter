![Build](https://github.com/ckgod/writerside-markdown-converter/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/28161.svg)](https://plugins.jetbrains.com/plugin/28161)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/28161.svg)](https://plugins.jetbrains.com/plugin/28161)

<!-- Plugin description -->
# Writerside Markdown Converter

-----

An intelligent plugin that converts plain text or Markdown into a format optimized for JetBrains Writerside. Powered by the Google Gemini API, it supports translating text from various languages and then converting it, streamlining the process of documenting and localizing technical articles.

### ScreenShot

![img.png](https://github.com/ckgod/writerside-markdown-converter/raw/main/images/example_usage.png)

### Features

* **AI-Powered Document Conversion**: Utilizes the Google Gemini API to automatically convert text into Writerside-compliant Markdown.
* **Multi-Language Translation & Conversion**: Supports translating text from various source languages into a selected target language before formatting.
* **Intuitive UI**: A dedicated Tool Window provides a seamless experience for input, conversion, and result verification all in one place.
* **Secure API Key Management**: Safely stores your Gemini API key in the OS keychain using the IntelliJ Platform's `PasswordSafe`.

### Usage

1.  Set up your Gemini API key, obtained from [Google AI Studio](https://aistudio.google.com/apikey), upon first launch.
2.  Select the target language, input your text, and click the 'Convert' button.

### Example

**Input Text:**

```
Warning: This is a critical update. All users must update before Friday.
For more information, see our website.
```

**Output (Writerside Markdown, after translating to Korean):**

```markdown
> 경고: 이것은 중요한 업데이트입니다.
> 모든 사용자는 금요일까지 업데이트해야 합니다.
{style="warning"}

더 자세한 정보는 저희 웹사이트를 참조하십시오.
```
![img.png](https://github.com/ckgod/writerside-markdown-converter/raw/main/images/warning_example.png)

### Installation

* **Via the built-in plugin system:**

  \<kbd\>Settings/Preferences\</kbd\> \> \<kbd\>Plugins\</kbd\> \> \<kbd\>Marketplace\</kbd\> \> \<kbd\>Search for "Writerside Markdown Converter"\</kbd\> \> \<kbd\>Install\</kbd\>

* **Via JetBrains Marketplace:**

  Navigate to the [JetBrains Marketplace page](https://plugins.jetbrains.com/plugin/28161) and click the \<kbd\>Install to...\</kbd\> button while your IDE is running.

### Change Log

[View the full changelog here.](https://github.com/ckgod/writerside-markdown-converter/blob/main/CHANGELOG.md)

### Contribute

Contributions of any kind are welcome, including bug reports, feature suggestions, or code contributions.

* [Issues](https://github.com/ckgod/writerside-markdown-converter/issues)
* [Pull Requests](https://github.com/ckgod/writerside-markdown-converter/pulls)

<!-- Plugin description end -->

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
