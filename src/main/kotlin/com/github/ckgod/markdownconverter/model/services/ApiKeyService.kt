package com.github.ckgod.markdownconverter.model.services

import com.intellij.credentialStore.CredentialAttributes
import com.intellij.ide.passwordSafe.PasswordSafe
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.components.Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Base64

@Service(Service.Level.APP)
class ApiKeyService {
    companion object {
        private const val API_KEY_PROPERTY = "com.github.ckgod.markdownconverter.apikey"
        private const val LEGACY_SERVICE_NAME = "com.github.ckgod.markdownconverter"
    }

    private val properties = PropertiesComponent.getInstance()

    private fun migrateFromPasswordSafe() {
        if (properties.getValue(API_KEY_PROPERTY) != null) {
            return
        }

        val credentialAttributes = CredentialAttributes(LEGACY_SERVICE_NAME)
        val oldApiKey = PasswordSafe.instance.getPassword(credentialAttributes)

        if (!oldApiKey.isNullOrEmpty()) {
            val encoded = Base64.getEncoder().encodeToString(oldApiKey.toByteArray())
            properties.setValue(API_KEY_PROPERTY, encoded)
            PasswordSafe.instance.set(credentialAttributes, null)
        }
    }

    fun getApiKey(): String? {
        migrateFromPasswordSafe()

        val encoded = properties.getValue(API_KEY_PROPERTY) ?: return null
        return try {
            String(Base64.getDecoder().decode(encoded))
        } catch (_: IllegalArgumentException) {
            null
        }
    }

    suspend fun saveApiKey(apiKey: String, onSuccess: (() -> Unit)? = null) = withContext(Dispatchers.IO) {
        val encoded = Base64.getEncoder().encodeToString(apiKey.toByteArray())
        properties.setValue(API_KEY_PROPERTY, encoded)
        onSuccess?.invoke()
    }

    fun deleteApiKey() {
        properties.unsetValue(API_KEY_PROPERTY)
    }
}