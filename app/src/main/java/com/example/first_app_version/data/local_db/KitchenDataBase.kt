//package com.example.first_app_version.data.local_db
//
//import android.content.Context
//import android.util.Log
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import androidx.sqlite.db.SupportSQLiteDatabase
//import com.example.first_app_version.R
//import com.example.first_app_version.data.models.Dish
//import com.example.first_app_version.data.models.DishType
//import com.example.first_app_version.data.models.Kitchen
//
//@Database(
//    entities = [Kitchen::class, DishType::class, Dish::class],
//    version = 4,
//    exportSchema = false
//)
//abstract class KitchenDataBase : RoomDatabase() {
//
//    abstract fun kitchensDao(): KitchenDao
//    abstract fun dishTypesDao(): DishTypeDao
//    abstract fun dishesDao(): DishDao
//
//    companion object {
//        @Volatile
//        private var instance: KitchenDataBase? = null
//
//        fun getDataBase(context: Context): KitchenDataBase =
//            instance ?: synchronized(this) {
//                val db = Room.databaseBuilder(
//                    context.applicationContext,
//                    KitchenDataBase::class.java,
//                    "bite_db"
//                )
//                    .allowMainThreadQueries()
//                    .addCallback(object : Callback() {
//                        override fun onCreate(db: SupportSQLiteDatabase) {
//                            super.onCreate(db)
//                            Log.d("DB_CREATE", "KitchenDataBase onCreate CALLED")
//
//                            //Kitchens Images
//                            val pizzaImg = R.drawable.pizza
//                            val pastaImg = R.drawable.pasta
//                            val sushiImg = R.drawable.sushi
//                            val lasagnaImg= R.drawable.lasagna
//
//                            //Betty
//                            val napoliPizzaImg = R.drawable.napoli_pizza;
//                            val pepperoniPizzaImg = R.drawable.pepperoni_pizza;
//                            val pineapplePizzaImg = R.drawable.pineapple_pizza;
//                            val pestoPizzaImg = R.drawable.pesto_pizza;
//                            val whitePizzaImg = R.drawable.white_cheese_pizza;
//
//
//                            // Seed Kitchens
//                            db.execSQL("INSERT INTO kitchens (id, name, image_res, description) VALUES (1, 'Italian', NULL, NULL)")
//                            db.execSQL("INSERT INTO kitchens (id, name, image_res, description) VALUES (2, 'Asian', NULL, NULL)")
//                            db.execSQL("INSERT INTO kitchens (id, name, image_res, description) VALUES (3, 'Vegan', NULL, NULL)")
//                            db.execSQL("INSERT INTO kitchens (id, name, image_res, description) VALUES (4, 'Meat', NULL, NULL)")
//                            db.execSQL("INSERT INTO kitchens (id, name, image_res, description) VALUES (5, 'Deserts', NULL, NULL)")
//
//
//
//                            // Seed Dish Types for Italian (kitchen_id = 1)
//                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (1, 1, 'Pizza', $pizzaImg, NULL)")
//                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (2, 1, 'Pasta', $pastaImg, NULL)")
//                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (3, 1, 'Lasagna', $lasagnaImg, NULL)")
//
//                            // Seed Dish Types for Asian (kitchen_id = 2)
//                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (4, 2, 'Sushi', NULL, NULL)")
//                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (5, 2, 'Dim Sum', NULL, NULL)")
//                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (6, 2, 'Ramen', NULL, NULL)")
//
//                            // Seed Dish Types for Vegan (kitchen_id = 3)
//                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (7, 3, 'Salads', NULL, NULL)")
//
//
//                            // Seed Dishes for Italian -> Pizza (dish_type_id = 1)
////                            db.execSQL("INSERT INTO dishes (id, dish_type_id, name, image_res, description) VALUES (1, 1, 'Neapolitan Pizza', NULL, 'Classic Neapolitan pizza with thin, soft crust.')")
////                            db.execSQL("INSERT INTO dishes (id, dish_type_id, name, image_res, description) VALUES (2, 1, 'Pepperoni Pizza', NULL, 'Crispier Roman-style pizza.')")
////                            db.execSQL("INSERT INTO dishes (id, dish_type_id, name, image_res, description) VALUES (3, 1, 'Pineapple Pizza', NULL, 'Thick-crust Sicilian square pizza.')")
//
//
//                            // Seed Dishes for Italian -> Pizza (dish_type_id = 1)
//                            db.execSQL(
//                                "INSERT INTO dishes (id, dish_type_id, name, image_res, description) VALUES " +
//                                        "(1, 1, 'Neapolitan Pizza | RISHON LEZION' ,'Vivino', $napoliPizzaImg, 'Traditional Neapolitan pizza with a soft, airy crust, fresh tomato sauce, mozzarella cheese, and basil.')"
//                            )
//
//                            db.execSQL(
//                                "INSERT INTO dishes (id, dish_type_id, name, image_res, description) VALUES " +
//                                        "(2, 1, 'Pepperoni Pizza | GIVATAYIM','Tony Vespa', $pepperoniPizzaImg, 'Classic pizza topped with mozzarella cheese, rich tomato sauce, and spicy pepperoni slices.')"
//                            )
//
//                            db.execSQL(
//                                "INSERT INTO dishes (id, dish_type_id, name, image_res, description) VALUES " +
//                                        "(3, 1, 'Pineapple Pizza | HERZLIYA','Pizza Yoav', $pineapplePizzaImg, 'Sweet and savory pizza with mozzarella cheese, tomato sauce, and juicy pineapple pieces.')"
//                            )
//
//                            db.execSQL(
//                                "INSERT INTO dishes (id, dish_type_id, name, image_res, description) VALUES " +
//                                        "(4, 1, 'Pesto Pizza | TLV','RUSTICO', $whitePizzaImg, 'Pizza topped with fresh basil pesto, mozzarella cheese, and a drizzle of olive oil.')"
//                            )
//
//                            db.execSQL(
//                                "INSERT INTO dishes (id, dish_type_id, name, image_res, description) VALUES " +
//                                        "(5, 1, 'White Pizza | TLV','FIFTY & ONE', $pestoPizzaImg, 'Creamy white pizza without tomato sauce, made with mozzarella, ricotta cheese, garlic, and olive oil.')"
//                            )
//
//
//                        }
//                    })
//                    .build()
//                instance = db
//                db
//            }
//    }
//}

package com.example.first_app_version.data.local_db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.first_app_version.R
import com.example.first_app_version.data.models.Dish
import com.example.first_app_version.data.models.DishType
import com.example.first_app_version.data.models.Kitchen

@Database(
    entities = [Kitchen::class, DishType::class, Dish::class],
    version = 4,
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
                val dbInstance = Room.databaseBuilder(
                    context.applicationContext,
                    KitchenDataBase::class.java,
                    "bite_db"
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Log.d("DB_CREATE", "KitchenDataBase onCreate CALLED")

                            // Kitchens Images
                            val pizzaImg = R.drawable.pizza
                            val pastaImg = R.drawable.pasta
                            val sushiImg = R.drawable.sushi
                            val lasagnaImg = R.drawable.lasagna_bolognese

                            // Dishes Images
                            //Pizza
                            val napoliPizzaImg = R.drawable.napoli_pizza
                            val pepperoniPizzaImg = R.drawable.pepperoni_pizza
                            val pineapplePizzaImg = R.drawable.pineapple_pizza
                            val pestoPizzaImg = R.drawable.pesto_pizza
                            val whitePizzaImg = R.drawable.white_cheese_pizza

                            //Pasta
                            val carbonaraImg = R.drawable.pasta_carbonara
                            val rosaImg = R.drawable.pasta_rosa
                            val alfredoImg = R.drawable.pasta_alfredo
                            val pestoPastaImg = R.drawable.pasta_pesto
                            val arrabiataImg = R.drawable.pasta_arrabiata

                            // Seed Dishes for Italian -> Lasagna (dish_type_id = 3)
                            val lasagnaBologneseImg = R.drawable.lasagna_bolognese
                            val lasagnaVegetableImg = R.drawable.lasagna_vegetable
                            val lasagnaSpinachRicottaImg = R.drawable.lasagna_spinach_ricotta
                            val lasagnaMushroomCreamImg = R.drawable.lasagna_mushroom_cream
                            val lasagnaEggplantParmesanImg = R.drawable.lasagna_eggplant_parmesan

                            // Sushi images (dish_type_id = 4)
                            val salmonNigiriImg = R.drawable.sushi_salmon_nigiri
                            val tunaNigiriImg = R.drawable.sushi_tuna_nigiri
                            val californiaRollImg = R.drawable.sushi_california_roll
                            val spicyTunaRollImg = R.drawable.sushi_spicy_tuna_roll
                            val tempuraRollImg = R.drawable.sushi_tempura_roll


                            // Seed Kitchens
                            db.execSQL("INSERT INTO kitchens (id, name, image_res, description) VALUES (1, 'Italian', NULL, NULL)")
                            db.execSQL("INSERT INTO kitchens (id, name, image_res, description) VALUES (2, 'Asian', NULL, NULL)")
                            db.execSQL("INSERT INTO kitchens (id, name, image_res, description) VALUES (3, 'Vegan', NULL, NULL)")
                            db.execSQL("INSERT INTO kitchens (id, name, image_res, description) VALUES (4, 'Meat', NULL, NULL)")
                            db.execSQL("INSERT INTO kitchens (id, name, image_res, description) VALUES (5, 'Deserts', NULL, NULL)")

                            // Seed Dish Types for Italian (kitchen_id = 1)
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (1, 1, 'Pizza', $pizzaImg, NULL)")
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (2, 1, 'Pasta', $pastaImg, NULL)")
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (3, 1, 'Lasagna', $lasagnaImg, NULL)")

                            // Seed Dish Types for Asian (kitchen_id = 2)
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (4, 2, 'Sushi', NULL, NULL)")
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (5, 2, 'Dim Sum', NULL, NULL)")
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (6, 2, 'Ramen', NULL, NULL)")

                            // Seed Dish Types for Vegan (kitchen_id = 3)
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (7, 3, 'Salads', NULL, NULL)")

                            // Seed Dishes for Italian -> Pizza (dish_type_id = 1)
                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(1, 1, 'Neapolitan Pizza', 'Vivino | Rishon LeZion', $napoliPizzaImg, 'Traditional Neapolitan pizza with a soft, airy crust, fresh tomato sauce, mozzarella cheese, and basil.')"
                            )
                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(2, 1, 'Pepperoni Pizza', 'Tony Vespa | Givatayim', $pepperoniPizzaImg, 'Classic pizza topped with mozzarella cheese, rich tomato sauce, and spicy pepperoni slices.')"
                            )
                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(3, 1, 'Pineapple Pizza', 'Pizza Yoav | Herzliya', $pineapplePizzaImg, 'Sweet and savory pizza with mozzarella cheese, tomato sauce, and juicy pineapple pieces.')"
                            )
                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(4, 1, 'Pesto Pizza', 'Rustico | Tel Aviv', $pestoPizzaImg, 'Pizza topped with fresh basil pesto, mozzarella cheese, and a drizzle of olive oil.')"
                            )
                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(5, 1, 'White Pizza', 'Fifty & One | Tel Aviv', $whitePizzaImg, 'Creamy white pizza without tomato sauce, made with mozzarella, ricotta cheese, garlic, and olive oil.')"
                            )

                            // Seed Dishes for Italian -> Pasta (dish_type_id = 2)
                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(6, 2, 'Pasta Carbonara', 'La Cucina | Haifa', $carbonaraImg, 'Classic Italian pasta with creamy egg-based sauce, parmesan cheese, and crispy pancetta.')"
                            )
                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(7, 2, 'Pasta Rosa', 'Caffe Italia | Ramat Gan', $rosaImg, 'Creamy tomato and cream sauce with a smooth, rich flavor, served over pasta.')"
                            )
                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(8, 2, 'Pasta Alfredo', 'Trattoria Toscana | Jerusalem', $alfredoImg, 'Rich white sauce made with butter, cream, and parmesan cheese, served over pasta.')"
                            )
                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(9, 2, 'Pasta Pesto', 'Vero Italiano | Netanya', $pestoPastaImg, 'Fresh basil pesto blended with olive oil and parmesan cheese, tossed with pasta.')"
                            )
                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(10, 2, 'Pasta Arrabiata', 'Roma Street | Be’er Sheva', $arrabiataImg, 'Spicy tomato-based sauce with garlic and chili peppers for a bold Italian flavor.')"
                            )


                            // Seed Dishes for Italian -> Lasagna (dish_type_id = 3)
                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(11, 3, 'Lasagna Bolognese', 'Casa Italia | Ashdod', $lasagnaBologneseImg, 'Classic layered lasagna with rich beef bolognese sauce, tomato sauce, and melted mozzarella cheese.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(12, 3, 'Vegetable Lasagna', 'Green Table | Ra’anana', $lasagnaVegetableImg, 'Oven-baked lasagna layered with seasonal vegetables, tomato sauce, and creamy cheese.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(13, 3, 'Spinach & Ricotta Lasagna', 'La Nonna | Kfar Saba', $lasagnaSpinachRicottaImg, 'Delicate lasagna layers filled with spinach, ricotta cheese, and light béchamel sauce.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(14, 3, 'Mushroom Cream Lasagna', 'Trattoria Bianca | Modiin', $lasagnaMushroomCreamImg, 'Creamy lasagna with sautéed mushrooms, béchamel sauce, and parmesan cheese.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(15, 3, 'Eggplant & Parmesan Lasagna', 'Bella Napoli | Bat Yam', $lasagnaEggplantParmesanImg, 'Layered lasagna with roasted eggplant, tomato sauce, parmesan cheese, and Italian herbs.')"
                            )


                        // Seed Dishes for Asian -> Sushi (dish_type_id = 4)
                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(16, 4, 'Salmon Nigiri', 'Taizu | Tel Aviv', $salmonNigiriImg, 'Fresh salmon served over seasoned sushi rice, topped with a light brush of soy-based glaze.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(17, 4, 'Tuna Nigiri', 'Nini Hachi | Herzliya', $tunaNigiriImg, 'Tender tuna slices placed on sushi rice for a clean, classic bite with delicate flavor.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(18, 4, 'California Roll', 'Sushi Room | Rishon LeZion', $californiaRollImg, 'Inside-out roll with crab-style filling, avocado, and cucumber, rolled with rice and sesame.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(19, 4, 'Spicy Tuna Roll', 'Sushi Samba | Haifa', $spicyTunaRollImg, 'Tuna mixed with spicy mayo, rolled with cucumber and rice for a bold and flavorful roll.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(20, 4, 'Tempura Roll', 'Japanika | Be’er Sheva', $tempuraRollImg, 'Crispy tempura-style filling with fresh vegetables, rolled and finished with a light sweet-savory sauce.')"
                            )
                        }
                    })
                    .build()

                instance = dbInstance
                dbInstance
            }
    }
}
