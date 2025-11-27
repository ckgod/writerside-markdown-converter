![Build](https://github.com/ckgod/writerside-markdown-converter/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/28161.svg)](https://plugins.jetbrains.com/plugin/28161)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/28161.svg)](https://plugins.jetbrains.com/plugin/28161)

<!-- Plugin description -->
# Writerside Markdown Converter

-----

An intelligent plugin that converts plain text or Markdown into a format optimized for JetBrains Writerside. Powered by the Google Gemini API, it supports translating text from various languages and then converting it, streamlining the process of documenting and localizing technical articles.

### ScreenShot

<img src="https://github.com/ckgod/writerside-markdown-converter/raw/main/images/example_usage.png" width="50%" alt="example usage">

### Features

* **AI-Powered Document Conversion**: Utilizes the Google Gemini API to automatically convert text into Writerside-compliant Markdown.
* **Multi-Language Translation & Conversion**: Supports translating text from various source languages into a selected target language before formatting.
* **Semantic Markup Support**: Automatically converts plain text into Writerside's semantic markup elements such as `<ui-path>`, `<shortcut>`, `<warning>`, and `<tabs>`, enabling rich documentation formatting with enhanced readability and interactive components.
* **Intuitive UI**: A dedicated Tool Window provides a seamless experience for input, conversion, and result verification all in one place.

### Usage

1.  Set up your Gemini API key, obtained from [Google AI Studio](https://aistudio.google.com/apikey), upon first launch.
2.  Select the target language, input your text, and click the 'Convert' button.

### Example

**Input Text:**

```
## Database Configuration

To configure the connection, navigate to File > Settings > Database.
You can press Ctrl+Alt+S to open settings quickly.

Warning: Do not share your production credentials in the chat.

If you are using PostgreSQL, set the port to 5432.
If you are using MySQL, set the port to 3306.
```

**Output (Writerside Markdown, Use Semantic Markup):**

```
## Database Configuration {#database-configuration}

To configure the connection, navigate to <ui-path>File | Settings | Database</ui-path>.
You can press <shortcut>Ctrl+Alt+S</shortcut> to open settings quickly.

<warning>
Do not share your production credentials in the chat.
</warning>

<tabs>
<tab title="PostgreSQL">
If you are using PostgreSQL, set the port to 5432.
</tab>
<tab title="MySQL">
If you are using MySQL, set the port to 3306.
</tab>
</tabs>
```

**Rendering Output ↓**

<img src="https://github.com/ckgod/writerside-markdown-converter/raw/main/images/example_rendering.png" width="50%" alt="example rendering">

### Installation

* **Via the built-in plugin system:**

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Writerside Markdown Converter"</kbd> > <kbd>Install</kbd>

* **Via JetBrains Marketplace:**

  Navigate to the [JetBrains Marketplace page](https://plugins.jetbrains.com/plugin/28161) and click the <kbd>Install to...</kbd> button while your IDE is running.

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
