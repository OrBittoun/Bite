package com.example.first_app_version.data.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.first_app_version.data.models.Dish
import com.example.first_app_version.data.models.DishType
import com.example.first_app_version.data.models.Kitchen

@Database(
    entities = [Kitchen::class, DishType::class, Dish::class],
    version = 3,
    exportSchema = false
)
abstract class KitchenDataBase : RoomDatabase() {

    abstract fun kitchensDao(): KitchenDao
    abstract fun dishTypesDao(): DishTypeDao
    abstract fun dishesDao(): DishDao

    companion object {
        @Volatile
        private var instance: KitchenDataBase? = null

        fun getDataBase(context: Context): KitchenDataBase =
            instance ?: synchronized(this) {
                val db = Room.databaseBuilder(
                    context.applicationContext,
                    KitchenDataBase::class.java,
                    "bite_db"
                )
                    .allowMainThreadQueries()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Seed Kitchens
                            db.execSQL("INSERT INTO kitchens (id, name, image_res, description) VALUES (1, 'Italian', NULL, NULL)")
                            db.execSQL("INSERT INTO kitchens (id, name, image_res, description) VALUES (2, 'Asian', NULL, NULL)")

                            //בטי הוסיפה
                            db.execSQL("INSERT INTO kitchens (id, name, image_res, description) VALUES (3, 'Vegan', NULL, NULL)")
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (7, 3, 'Salads', NULL, NULL)")
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (8, 3, 'shakes', NULL, NULL)")
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (9, 3, 'Burgers', NULL, NULL)")

                            // Seed Dish Types for Italian (kitchen_id = 1)
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (1, 1, 'Pizza', NULL, NULL)")
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (2, 1, 'Pasta', NULL, NULL)")
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (3, 1, 'Lasagna', NULL, NULL)")

                            // Seed Dish Types for Asian (kitchen_id = 2)
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (4, 2, 'Sushi', NULL, NULL)")
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (5, 2, 'Dim Sum', NULL, NULL)")
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (6, 2, 'Ramen', NULL, NULL)")

                            // Seed Dishes for Italian -> Pizza (dish_type_id = 1)
                            db.execSQL("INSERT INTO dishes (id, dish_type_id, name, image_res, description) VALUES (1, 1, 'Neapolitan', NULL, 'Classic Neapolitan pizza with thin, soft crust.')")
                            db.execSQL("INSERT INTO dishes (id, dish_type_id, name, image_res, description) VALUES (2, 1, 'Roman', NULL, 'Crispier Roman-style pizza.')")
                            db.execSQL("INSERT INTO dishes (id, dish_type_id, name, image_res, description) VALUES (3, 1, 'Sicilian', NULL, 'Thick-crust Sicilian square pizza.')")
                        }
                    })
                    .build()
                instance = db
                db
            }
    }
}