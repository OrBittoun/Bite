package com.example.first_app_version.di

import android.content.Context
import androidx.room.Room
import com.example.first_app_version.data.local_db.CategoryDao
import com.example.first_app_version.data.local_db.CommentDao
import com.example.first_app_version.data.local_db.DishDao
import com.example.first_app_version.data.local_db.DishTypeDao
import com.example.first_app_version.data.local_db.KitchenDao
import com.example.first_app_version.data.local_db.KitchenDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideKitchenDatabase(
        @ApplicationContext context: Context
    ): KitchenDataBase {
        return Room.databaseBuilder(
            context,
            KitchenDataBase::class.java,
            "bite_db"
        )
            .fallbackToDestructiveMigration()
            .addCallback(KitchenDataBase.buildCallback())
            .build()
    }

    @Provides
    fun provideKitchenDao(db: KitchenDataBase): KitchenDao = db.kitchensDao()

    @Provides
    fun provideDishTypeDao(db: KitchenDataBase): DishTypeDao = db.dishTypesDao()

    @Provides
    fun provideDishDao(db: KitchenDataBase): DishDao = db.dishesDao()

    @Provides
    fun provideCommentDao(db: KitchenDataBase): CommentDao = db.commentsDao()

    @Provides
    fun provideCategoryDao(db: KitchenDataBase): CategoryDao = db.categoryDao()
}