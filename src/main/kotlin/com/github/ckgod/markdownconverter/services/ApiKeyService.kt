package com.github.ckgod.markdownconverter.services

import com.intellij.credentialStore.CredentialAttributes
import com.intellij.credentialStore.Credentials
import com.intellij.ide.passwordSafe.PasswordSafe
import com.intellij.openapi.components.Service


@Service(Service.Level.APP)
class ApiKeyService {
    companion object {
        private const val SERVICE_NAME = "com.github.ckgod.markdownconverter"
    }

    private val credentialAttributes = CredentialAttributes(SERVICE_NAME)

    fun getApiKey(): String? = PasswordSafe.instance.getPassword(credentialAttributes)

    fun saveApiKey(apiKey: String?) {
        val credentials = if (apiKey.isNullOrBlank()) null else Credentials("", apiKey)
        PasswordSafe.instance.set(credentialAttributes, credentials)
    }
}