package com.stfalcon.new_cookorama_android.injection.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.stfalcon.new_uaroads_android.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/*
 * Created by Anton Bevza on 3/28/17.
 */
@Module
open class NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Provides
    @Singleton
    fun provideNetworkInterceptor(): Interceptor = Interceptor { chain ->
        val original = chain.request()
        val builder: Request.Builder = original.newBuilder()
                .url(original.url())
        builder.addHeader("Accept", "application/json")
        val request = builder.build()
        chain.proceed(request)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(networkInterceptor: Interceptor,
                            loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
            OkHttpClient.Builder()
                    .addNetworkInterceptor(networkInterceptor)
                    .addInterceptor(loggingInterceptor)
                    .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit =
            Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build()
}