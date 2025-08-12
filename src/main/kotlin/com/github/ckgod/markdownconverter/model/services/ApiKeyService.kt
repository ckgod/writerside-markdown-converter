package com.github.ckgod.markdownconverter.model.services

import com.intellij.credentialStore.CredentialAttributes
import com.intellij.credentialStore.Credentials
import com.intellij.ide.passwordSafe.PasswordSafe
import com.intellij.openapi.components.Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Service(Service.Level.APP)
class ApiKeyService {
    companion object {
        private const val SERVICE_NAME = "com.github.ckgod.markdownconverter"
    }

    private val credentialAttributes = CredentialAttributes(SERVICE_NAME)

    fun getApiKey(): String? = PasswordSafe.instance.getPassword(credentialAttributes)

    suspend fun saveApiKey(apiKey: String, onSuccess: (() -> Unit)? = null) = withContext(Dispatchers.IO) {
        val credentials = Credentials("", apiKey)
        PasswordSafe.instance.set(credentialAttributes, credentials)
        onSuccess?.invoke()
    }
}