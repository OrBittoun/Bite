package com.example.first_app_version.di

import com.example.first_app_version.data.models.FoodApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            // זו כתובת בסיס דמיונית כרגע לצורך ההדגמה
            .baseUrl("https://api.mock-food-quotes.com/")
            .addConverterFactory(GsonConverterFactory.create()) // הממיר מ-JSON לקוטלין
            .build()
    }

    @Provides
    @Singleton
    fun provideFoodApi(retrofit: Retrofit): FoodApi {
        return retrofit.create(FoodApi::class.java)
    }
}