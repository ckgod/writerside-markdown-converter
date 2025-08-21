![Build](https://github.com/ckgod/writerside-markdown-converter/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/28161.svg)](https://plugins.jetbrains.com/plugin/28161)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/28161.svg)](https://plugins.jetbrains.com/plugin/28161)

<!-- Plugin description -->
# Writerside Markdown Converter

-----

An intelligent plugin that converts plain text or Markdown into a format optimized for JetBrains Writerside. Powered by the Google Gemini API, it supports translating foreign text into Korean before conversion.
<br><br>
일반 텍스트나 마크다운을 JetBrains Writerside에 최적화된 형식으로 변환하는 IntelliJ 플러그인입니다. Google Gemini API를 기반으로 동작하며, **영문 등의 외국어 텍스트를 한국어로 번역 후 변환**하는 기능을 지원하여 해외 기술 문서의 정리 및 작성을 돕습니다.

### ScreenShot

![img.png](https://github.com/ckgod/writerside-markdown-converter/raw/main/images/example_usage.png)

### Features

* **AI 기반 문서 변환**: Google Gemini API를 사용하여 텍스트를 Writerside 문법으로 자동 변환
* **다국어 번역 및 변환**: 외국어 텍스트를 **한국어로 자동 번역**한 후 Writerside 문법으로 변환
* **직관적인 UI**: Tool Window 내에서 입력, 변환, 결과 확인까지 모든 작업을 한 번에 처리
* **안전한 API 키 관리**: IntelliJ `PasswordSafe`를 통해 Gemini API 키를 OS 키체인에 안전하게 암호화하여 저장

### Usage

1. 최초 실행 시 [Google AI Studio](https://aistudio.google.com/apikey)에서 발급받은 Gemini API 키 설정
2. 텍스트 입력 후 'Convert' 버튼 클릭

### Example

**Input Text:**

```
Warning: This is a critical update. All users must update before Friday.
For more information, see our website.
```

**Output (Writerside Markdown):**

```markdown
> 경고: 이것은 중요한 업데이트입니다.
> 모든 사용자는 금요일까지 업데이트해야 합니다.
{style="warning"}

더 자세한 정보는 저희 웹사이트를 참조하십시오.
```
![img.png](https://github.com/ckgod/writerside-markdown-converter/raw/main/images/warning_example.png)

### installation

- **IDE 내장 플러그인 시스템 사용**:

    <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Writerside Markdown Converter"</kbd> >
  <kbd>Install</kbd>

- **JetBrains Marketplace 사용**:

  [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/28161)로 이동하여 IDE가 실행 중인 경우 <kbd>Install to ...</kbd> 버튼을 클릭하여 설치.


### Change Log

[ChangeLog](https://github.com/ckgod/writerside-markdown-converter/blob/main/CHANGELOG.md)

### Contribute

버그 리포트, 기능 제안, 코드 기여 등 어떤 종류의 기여든 환영합니다.

* [Issues](https://github.com/ckgod/writerside-markdown-converter/issues)
* [Pull Requests](https://github.com/ckgod/writerside-markdown-converter/pulls)

<!-- Plugin description end -->

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
