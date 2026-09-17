package com.example.cst438_project1_team5.api.malAPI

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MalOAuthService {
    data class malTokenResponse(
        val token_type: String,
        val expires_in: Int,
        val access_token: String,
        val refresh_token: String
    )

    @FormUrlEncoded
    @POST("v1/oauth2/token")
    suspend fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("grant_type") grantType: String = "authorization_code",
        @Field("code") code: String,
        @Field("code_verifier") codeVerifier: String,
        @Field("redirect_uri") redirectUri: String
    ) : malTokenResponse
}