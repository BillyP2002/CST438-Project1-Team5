package com.example.cst438_project1_team5.api.malAPI

import android.util.Base64
import java.security.SecureRandom
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.edit

class MalOAuthManager (private val context: Context){
    companion object {
        private const val CLIENT_ID = "98c1359eecdd8abb0c397113b580ce15"

        private const val REDIRECT_URI =
            "cst438project1team5://oauth"

        private const val PREFS_NAME = "mal_oauth_prefs"
        private const val PREF_CODE_VERIFIER = "code_verifier"
        private const val PREF_STATE = "state"
    }

    fun linkMalAccount() {

        val codeVerifier = getCodeVerifier()

        val state = getCodeVerifier()

        context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ).edit {
            putString(PREF_CODE_VERIFIER, codeVerifier)
            putString(PREF_STATE, state)
        }

        // Build MAL authorization URL
        val authorizationUri = Uri.parse(
            "https://myanimelist.net/v1/oauth2/authorize"
        ).buildUpon()
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("client_id", CLIENT_ID)
            .appendQueryParameter("state", state)
            .appendQueryParameter("redirect_uri", REDIRECT_URI)
            .appendQueryParameter("code_challenge", codeVerifier)
            .appendQueryParameter("code_challenge_method", "plain")
            .build()

        // Open MAL in the user's browser
        val intent = Intent(
            Intent.ACTION_VIEW,
            authorizationUri
        )

        context.startActivity(intent)
    }

    //Necessary to obtain an OAuth token, which is necessary as a whole for the MAL API.
    //Code verifier is identical to the code challenge under MAL API.
    fun getCodeVerifier() : String{
        val bytes = ByteArray(32)
        SecureRandom().nextBytes(bytes)

        return Base64.encodeToString(
            bytes,
            Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
        )
    }
}