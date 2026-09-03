//Retrofit Instance
object RetrofitClient {
    private const val BASE_URL = "https://api.myanimelist.net/v2"
    // Create logging interceptor for debugging
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    // Create OkHttp client with interceptor
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
    // Create Retrofit instance
    val api: ApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}

