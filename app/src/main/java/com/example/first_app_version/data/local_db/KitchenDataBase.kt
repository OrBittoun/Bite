
package com.example.first_app_version.data.local_db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.first_app_version.R
import com.example.first_app_version.data.models.Comment
import com.example.first_app_version.data.models.Dish
import com.example.first_app_version.data.models.DishType
import com.example.first_app_version.data.models.Kitchen

@Database(
    entities = [Kitchen::class, DishType::class, Dish::class, Comment::class],
    version = 8,
    exportSchema = false
)
abstract class KitchenDataBase : RoomDatabase() {

    abstract fun kitchensDao(): KitchenDao
    abstract fun dishTypesDao(): DishTypeDao
    abstract fun dishesDao(): DishDao
    abstract fun commentsDao(): CommentDao

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
                    .fallbackToDestructiveMigration(true)
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Log.d("DB_CREATE", "KitchenDataBase onCreate CALLED")
                            seedInitialData(db)
                        }

                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            // If a restored DB exists at v4 but is empty, seed now.
                            val cursor = db.query("SELECT COUNT(*) FROM kitchens")
                            cursor.use {
                                if (it.moveToFirst()) {
                                    val count = it.getInt(0)
                                    Log.d("DB_OPEN", "kitchens count=$count")
                                    if (count == 0) {
                                        Log.d("DB_OPEN", "kitchens empty, seeding initial data")
                                        seedInitialData(db)
                                    }
                                }
                            }
                        }

                        private fun seedInitialData(db: SupportSQLiteDatabase) {
                            // Kitchens Images
//                            val pizzaImg = R.drawable.napoli_pizza
//                            val pastaImg = R.drawable.pasta_rosa
//                            val lasagnaImg = R.drawable.lasagna_bolognese
//
//                            val ramenImg = R.drawable.ramen
//                            val dimSumImg = R.drawable.dim_sum_steamed_buns
//
//                            val veganSaladsImg = R.drawable.vegan_green_salad
//                            val veganBowlsImg = R.drawable.vegan_buddha_bowl
//                            val veganMainsImg = R.drawable.vegan_lentil_stew


                            // Dishes Images
                            //Seed Dishes for Italian -> Pizza (dish_type_id = 1)
                            val napoliPizzaImg = R.drawable.napoli_pizza
                            val pepperoniPizzaImg = R.drawable.pepperoni_pizza
                            val pineapplePizzaImg = R.drawable.pineapple_pizza
                            val pestoPizzaImg = R.drawable.pesto_pizza
                            val whitePizzaImg = R.drawable.white_cheese_pizza

                            //Seed Dishes for Italian -> Pasta (dish_type_id = 2)
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
                            val sushiImg = R.drawable.sushi


                            // Dim Sum images (dish_type_id = 5)
                            val shrimpDumplingsImg = R.drawable.dim_sum_shrimp_dumplings
                            val vegetableDumplingsImg = R.drawable.dim_sum_vegetable_dumplings
                            val steamedBunsImg = R.drawable.dim_sum_steamed_buns
                            val beefDumplingsImg = R.drawable.dim_sum_beef_dumplings
                            val chickenWontonsImg = R.drawable.dim_sum_chicken_wontons

                            // Ramen images (dish_type_id = 6)
                            val tonkotsuRamenImg = R.drawable.ramen_tonkotsu
                            val misoRamenImg = R.drawable.ramen_miso
                            val shoyuRamenImg = R.drawable.ramen_shoyu
                            val spicyRamenImg = R.drawable.ramen_spicy
                            val vegetableRamenImg = R.drawable.ramen_vegetable

                            // Bowls images (dish_type_id = 7)
                            val buddhaBowlImg = R.drawable.vegan_buddha_bowl
                            val falafelBowlImg = R.drawable.vegan_falafel_bowl
                            val tofuBowlImg = R.drawable.vegan_tofu_bowl
                            val mediterraneanBowlImg = R.drawable.vegan_mediterranean_bowl
                            val sweetPotatoBowlImg = R.drawable.vegan_sweet_potato_bowl

                            // Salad images(dish_type_id = 8)
                            val quinoaSaladImg = R.drawable.vegan_quinoa_salad
                            val greenSaladImg = R.drawable.vegan_green_salad
                            val chickpeaSaladImg = R.drawable.vegan_chickpea_salad
                            val roastedVegSaladImg = R.drawable.vegan_roasted_vegetable_salad
                            val avocadoSaladImg = R.drawable.vegan_avocado_salad

                            //Vegan Mains images (dish_type_id = 9)
                            val lentilStewImg = R.drawable.vegan_lentil_stew
                            val stuffedPeppersImg = R.drawable.vegan_stuffed_peppers
                            val eggplantPlateImg = R.drawable.vegan_eggplant_plate
                            val veggieMeatballsImg = R.drawable.vegan_veggie_meatballs
                            val stuffedZucchiniImg = R.drawable.vegan_stuffed_zucchini

                            // Meat Grill images (dish_type_id = 10)
                            val ribeyeImg = R.drawable.meat_ribeye
                            val entrecoteImg = R.drawable.meat_entrecote
                            val lambChopsImg = R.drawable.meat_lamb_chops
                            val beefSkewersImg = R.drawable.meat_beef_skewers

                            // Meat Burgers images (dish_type_id = 11)
                            val classicBurgerImg = R.drawable.meat_classic_burger
                            val cheeseBurgerImg = R.drawable.meat_cheese_burger
                            val bbqBurgerImg = R.drawable.meat_bbq_burger
                            val doubleBurgerImg = R.drawable.meat_double_burger

                            //Meat Stews images (dish_type_id = 12)
                            val beefStewImg = R.drawable.meat_beef_stew
                            val lambStewImg = R.drawable.meat_lamb_stew
                            val goulashImg = R.drawable.meat_goulash
                            val ossobucoImg = R.drawable.meat_ossobuco

                            // Meat Chicken images (dish_type_id = 13)
                            val grilledChickenImg = R.drawable.grilled_chicken
                            val schnitzelImg = R.drawable.schnitzel
                            val chickenSkewersImg = R.drawable.chicken_skewers
                            val chickenNuggetsImg = R.drawable.chicken_nuggets

                            // Fish images (dish_type_id = 14)
                            val grilledSalmonImg = R.drawable.fish_grilled_salmon
                            val seabassFilletImg = R.drawable.fish_seabass
                            val salmonTeriyakiImg = R.drawable.fish_teriyaki
                            val fishAndChipsImg = R.drawable.fish_and_chips

                            // לCakes images (dish_type_id = 15
                            val chocolateCakeImg = R.drawable.chocolate_cake
                            val cheesecakeImg = R.drawable.cheesecake
                            val carrotCakeImg = R.drawable.carrot_cake
                            val lavaCakeImg = R.drawable.lava_cake

                            // Ice Cream & Frozen images (dish_type_id = 16)
                            val vanillaIceCreamImg = R.drawable.vanilla_ice_cream
                            val chocolateIceCreamImg = R.drawable.chocolate_ice_cream
                            val strawberrySorbetImg = R.drawable.strawberry_sorbet
                            val frozenYogurtImg = R.drawable.frozen_yogurt

                            // Pastries images (dish_type_id = 17)
                            val croissantImg = R.drawable.croissant
                            val chocolateCroissantImg = R.drawable.chocolate_croissant
                            val appleTartImg = R.drawable.apple_tart
                            val cinnamonRollImg = R.drawable.cinnamon_roll


                            // Seed Kitchens
                            db.execSQL("INSERT INTO kitchens (id, name, image_res, description) VALUES (1, 'Italian', NULL, NULL)")
                            db.execSQL("INSERT INTO kitchens (id, name, image_res, description) VALUES (2, 'Asian', NULL, NULL)")
                            db.execSQL("INSERT INTO kitchens (id, name, image_res, description) VALUES (3, 'Vegan', NULL, NULL)")
                            db.execSQL("INSERT INTO kitchens (id, name, image_res, description) VALUES (4, 'Meat & Fish', NULL, NULL)")
                            db.execSQL("INSERT INTO kitchens (id, name, image_res, description) VALUES (5, 'Desserts', NULL, NULL)")

                            // Seed Dish Types for Italian (kitchen_id = 1)
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (1, 1, 'Pizza', $pepperoniPizzaImg, NULL)")
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (2, 1, 'Pasta', $rosaImg, NULL)")
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (3, 1, 'Lasagna', $lasagnaBologneseImg, NULL)")

                            // Seed Dish Types for Asian (kitchen_id = 2)
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (4, 2, 'Sushi', $sushiImg, NULL)")
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (5, 2, 'Dim Sum', $steamedBunsImg, NULL)")
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (6, 2, 'Ramen', $spicyRamenImg, NULL)")

                            // Seed Dish Types for Vegan (kitchen_id = 3)
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (7, 3, 'Vegan Bowls', $buddhaBowlImg, NULL)")
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (8, 3, 'Vegan Salads', $greenSaladImg, NULL)")
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (9, 3, 'Vegan Mains', $lentilStewImg, NULL)")

                            // Seed Dish Types for Meat & Fish (kitchen_id = 4)
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (10, 4, 'Grill', $beefSkewersImg, NULL)")
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (11, 4, 'Burgers', $doubleBurgerImg, NULL)")
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (12, 4, 'Stews', $lambStewImg, NULL)")
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (13, 4, 'Chicken', $schnitzelImg, NULL)")
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (14, 4, 'Fish', $fishAndChipsImg, NULL)")


                            // Seed Dish Types for Desserts (kitchen_id = 5)
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (15, 5, 'Cakes', $cheesecakeImg, NULL)")
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (16, 5, 'Ice Cream & Frozen', $frozenYogurtImg, NULL)")
                            db.execSQL("INSERT INTO dish_types (id, kitchen_id, name, image_res, description) VALUES (17, 5, 'Pastries', $appleTartImg, NULL)")



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


                            // Seed Dishes for Asian -> Dim Sum (dish_type_id = 5)
                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(21, 5, 'Shrimp Dumplings', 'China Court | Tel Aviv', $shrimpDumplingsImg, 'Delicate steamed dumplings filled with shrimp, served with a light soy-based dipping sauce.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(22, 5, 'Vegetable Dumplings', 'Green Wok | Ra’anana', $vegetableDumplingsImg, 'Steamed dumplings filled with mixed vegetables and Asian spices, light and flavorful.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(23, 5, 'Steamed Buns', 'Little Asia | Ramat Gan', $steamedBunsImg, 'Soft steamed buns filled with savory meat and sauce, warm and comforting.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(24, 5, 'Beef Dumplings', 'Red Dragon | Haifa', $beefDumplingsImg, 'Juicy beef-filled dumplings steamed to perfection with aromatic herbs and spices.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(25, 5, 'Chicken Wontons', 'Asia Kitchen | Ashkelon', $chickenWontonsImg, 'Light wontons filled with seasoned chicken, served steamed with a delicate dipping sauce.')"
                            )

                            // Seed Dishes for Asian -> Ramen (dish_type_id = 6)
                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(26, 6, 'Tonkotsu Ramen', 'Japanika | Tel Aviv', $tonkotsuRamenImg, 'Rich pork-based broth simmered for hours, served with noodles, sliced pork, and green onions.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(27, 6, 'Miso Ramen', 'Ramen Ya | Jerusalem', $misoRamenImg, 'Savory miso-based broth with noodles, vegetables, and a deep, comforting umami flavor.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(28, 6, 'Shoyu Ramen', 'Nini Hachi | Herzliya', $shoyuRamenImg, 'Soy sauce-based broth with a clear, balanced taste, served with noodles and classic toppings.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(29, 6, 'Spicy Ramen', 'Sushi Samba | Haifa', $spicyRamenImg, 'Spicy chili-infused broth with noodles and vegetables for a bold and warming kick.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(30, 6, 'Vegetable Ramen', 'Green Asia | Ramat Gan', $vegetableRamenImg, 'Light vegetable-based broth with noodles, mushrooms, greens, and aromatic herbs.')"
                            )


                            // Seed Dishes for Vegan -> Bowls (dish_type_id = 7)
                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(31, 7, 'Buddha Bowl', 'Anastasia | Tel Aviv', $buddhaBowlImg, 'Colorful vegan bowl with roasted vegetables, grains, chickpeas, and tahini dressing.')"
                            )
                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(32, 7, 'Falafel Bowl', 'Abu Hassan | Tel Aviv', $falafelBowlImg, 'Falafel balls served with hummus, fresh vegetables, tahini, and warm grains.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(33, 7, 'Tofu Bowl', 'Green Panda | Ramat Gan', $tofuBowlImg, 'Marinated tofu with rice, steamed vegetables, and sesame soy dressing.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(34, 7, 'Mediterranean Bowl', 'Levinsky Market | Tel Aviv', $mediterraneanBowlImg, 'Mediterranean-style bowl with chickpeas, roasted vegetables, herbs, and olive oil.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(35, 7, 'Sweet Potato Bowl', 'Roots & Greens | Herzliya', $sweetPotatoBowlImg, 'Roasted sweet potato served with quinoa, greens, tahini, and pumpkin seeds.')"
                            )


                            // Seed Dishes for Vegan -> Salads (dish_type_id = 8)
                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(36, 8, 'Fresh Green Salad', 'Green Leaf | Tel Aviv', $greenSaladImg, 'Crisp mixed greens with cucumber, herbs, lemon juice, and olive oil.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(37, 8, 'Chickpea Salad', 'Roots | Ramat Gan', $chickpeaSaladImg, 'Protein-rich chickpea salad with tomatoes, parsley, red onion, and fresh lemon dressing.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(38, 8, 'Roasted Vegetable Salad', 'Urban Vegan | Herzliya', $roastedVegSaladImg, 'Warm roasted vegetables served over fresh greens with balsamic vinaigrette.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(39, 8, 'Avocado Salad', 'Pure Kitchen | Kfar Saba', $avocadoSaladImg, 'Fresh avocado slices with mixed greens, cherry tomatoes, and a light citrus dressing.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(40, 8, 'Quinoa Salad', 'Green Cat | Ramat Gan', $quinoaSaladImg, 'Fresh quinoa salad with herbs, vegetables, lemon juice, and olive oil.')"
                            )


                            // Seed Dishes for Vegan -> Mains (dish_type_id = 9)
                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(41, 9, 'Lentil Stew', 'Meshek Barzilay | Tel Aviv', $lentilStewImg, 'Slow-cooked lentil stew with vegetables and warming Middle Eastern spices.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(42, 9, 'Stuffed Peppers', 'Nana | Haifa', $stuffedPeppersImg, 'Bell peppers stuffed with rice, herbs, and vegetables, baked in tomato sauce.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(43, 9, 'Roasted Eggplant Plate', 'Opa | Jerusalem', $eggplantPlateImg, 'Roasted eggplant served with tahini, tomato salsa, herbs, and olive oil.')"
                            )
                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(44, 9, 'Vegetable Meatballs', 'Anastasia | Tel Aviv', $veggieMeatballsImg, 'Baked vegetable-based meatballs served with tomato sauce and fresh herbs.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(45, 9, 'Stuffed Zucchini', 'Green Spot | Raanana', $stuffedZucchiniImg, 'Zucchini stuffed with rice, herbs, and vegetables, simmered in a light tomato sauce.')"
                            )


                            // Seed Dishes for Meat & Fish -> Grill (dish_type_id = 10)
                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(46, 10, 'Beef Skewers', 'Ashkara Grill | Ashdod', $beefSkewersImg, 'Tender beef skewers grilled over open flame.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(47, 10, 'Ribeye Steak', 'Hudson | Tel Aviv', $ribeyeImg, 'Juicy ribeye steak grilled to perfection with coarse salt and herbs.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(48, 10, 'Entrecôte Steak', 'Meat Bar | Rishon LeZion', $entrecoteImg, 'Classic grilled entrecôte steak served with roasted vegetables.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(49, 10, 'Lamb Chops', 'M25 | Tel Aviv', $lambChopsImg, 'Grilled lamb chops seasoned with garlic, rosemary, and olive oil.')"
                            )


                            // Seed Dishes for Meat & Fish -> Burgers (dish_type_id = 11)
                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(50, 11, 'Classic Burger', 'Burger Room | Tel Aviv', $classicBurgerImg, 'Beef patty served with lettuce, tomato, and house sauce.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(51, 11, 'Cheeseburger', 'BBB | Herzliya', $cheeseBurgerImg, 'Juicy beef burger topped with melted cheddar cheese.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(52, 11, 'BBQ Burger', 'Burger Saloon | Netanya', $bbqBurgerImg, 'Beef burger with smoky BBQ sauce and caramelized onions.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(53, 11, 'Double Burger', 'Moses | Jerusalem', $doubleBurgerImg, 'Two beef patties stacked with cheese and signature sauce.')"
                            )


                            // Seed Dishes for Meat & Fish -> Stews (dish_type_id = 12)
                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(54, 12, 'Beef Stew', 'Mitti | Haifa', $beefStewImg, 'Slow-cooked beef stew with vegetables and rich gravy.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(55, 12, 'Lamb Stew', 'HaBokrim | Be’er Sheva', $lambStewImg, 'Tender lamb cooked slowly with herbs and root vegetables.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(56, 12, 'Goulash', 'Hungarian House | Tel Aviv', $goulashImg, 'Traditional goulash with beef, paprika, and potatoes.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(57, 12, 'Osso Buco', 'Trattoria Toscana | Jerusalem', $ossobucoImg, 'Braised veal shank cooked in tomato and wine sauce.')"
                            )


                            // Seed Dishes for Meat & Fish -> Chicken (dish_type_id = 13)
                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(58, 13, 'Grilled Chicken Breast', 'Hudson | Tel Aviv', $grilledChickenImg, 'Grilled chicken breast seasoned with herbs and olive oil.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(59, 13, 'Chicken Schnitzel', 'Cafe Cafe | Ramat Gan', $schnitzelImg, 'Crispy breaded chicken schnitzel served golden and crunchy.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(60, 13, 'Chicken Skewers', 'Ashkara Grill | Ashdod', $chickenSkewersImg, 'Marinated chicken skewers grilled over charcoal.')"
                            )


                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(61, 13, 'Chicken Nuggets', 'Burger Ranch | Tel Aviv', $chickenNuggetsImg, 'Crispy golden chicken nuggets served with dipping sauce, crunchy on the outside and tender inside.')"
                            )


                            // Seed Dishes for Meat & Fish -> Grill (dish_type_id = 14)
                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(62, 14, 'Grilled Salmon', 'Blue Wave | Tel Aviv', $grilledSalmonImg, 'Fresh salmon fillet grilled with herbs, lemon, and olive oil.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(63, 14, 'Sea Bass Fillet', 'Ocean Grill | Herzliya', $seabassFilletImg, 'Pan-seared sea bass with seasonal vegetables and citrus sauce.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(64, 14, 'Teriyaki Salmon', 'Tokyo Fish | Ramat Gan', $salmonTeriyakiImg, 'Salmon glazed with sweet teriyaki sauce served with rice and greens.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(65, 14, 'Fish and Chips', 'London Fish Bar | Tel Aviv', $fishAndChipsImg, 'Crispy battered fish served with golden fries and tartar sauce.')"
                            )

                            // Seed Dishes for Desserts -> Cakes (dish_type_id = 15)
                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(66, 15, 'Chocolate Cake', 'Sweet Home | Tel Aviv', $chocolateCakeImg, 'Rich chocolate cake with creamy chocolate frosting.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(67, 15, 'Cheesecake', 'Dolce Vita | Ramat Gan', $cheesecakeImg, 'Classic baked cheesecake with a buttery biscuit base.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(68, 15, 'Carrot Cake', 'Sweet Corner | Herzliya',$carrotCakeImg, 'Moist carrot cake with cream cheese frosting.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(69, 15, 'Chocolate Lava Cake', 'Cocoa Room | Tel Aviv', $lavaCakeImg, 'Warm chocolate cake with a rich molten center.')"
                            )

                            // Seed Dishes for Desserts -> Ice Cream & Frozen (dish_type_id = 16)

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(70, 16, 'Vanilla Ice Cream', 'Gelato House | Tel Aviv', $vanillaIceCreamImg, 'Classic vanilla ice cream made with real vanilla beans.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(71, 16, 'Chocolate Ice Cream', 'Sweet Ice | Rishon LeZion', $chocolateIceCreamImg, 'Rich and creamy chocolate ice cream.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(72, 16, 'Strawberry Sorbet', 'Fruity Bar | Tel Aviv', $strawberrySorbetImg, 'Refreshing dairy-free strawberry sorbet.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(73, 16, 'Frozen Yogurt', 'Yogo | Herzliya', $frozenYogurtImg, 'Light frozen yogurt with fresh fruit toppings.')"
                            )

                            // Seed Dishes for Desserts -> Pastries (dish_type_id = 17)
                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(74, 17, 'Croissant', 'Paris Bakery | Tel Aviv', $croissantImg, 'Buttery French croissant with flaky layers.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(75, 17, 'Chocolate Croissant', 'Le Petit Paris | Ramat Gan', $chocolateCroissantImg, 'Flaky croissant filled with rich chocolate.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(76, 17, 'Apple Tart', 'Maison Douce | Tel Aviv', $appleTartImg, 'Classic French apple tart with buttery crust.')"
                            )

                            db.execSQL(
                                "INSERT INTO dishes (id, dish_type_id, name, restaurantName, image_res, description) VALUES " +
                                        "(77, 17, 'Cinnamon Roll', 'Sweet Corner | Haifa', $cinnamonRollImg, 'Soft cinnamon roll topped with vanilla icing.')"
                            )

                            // Seed Comments for Neapolitan Pizza dish (dish_id = 1)
                            db.execSQL("INSERT INTO comments (dish_id, author_name, rating, text, created_at, upvotes) VALUES (1, 'Tal', 5, 'Absolutely loved it — authentic and flavorful.', '20-12-2025, 10:05', 0)")
                            db.execSQL("INSERT INTO comments (dish_id, author_name, rating, text, created_at, upvotes) VALUES (1, 'Maya', 3, 'Good, but a bit too salty for me.', '20-12-2025, 12:30', 0)")
                            db.execSQL("INSERT INTO comments (dish_id, author_name, rating, text, created_at, upvotes) VALUES (1, 'Noam', 4, 'Great crust, could use more basil.', '20-12-2025, 18:45', 0)")

                            // Seed Comments for Pepperoni Pizza dish(dish_id = 2)
                            db.execSQL("INSERT INTO comments (dish_id, author_name, rating, text, created_at, upvotes) VALUES (2, 'Tal', 5, 'Absolutely loved it — authentic and flavorful.', '20-12-2025, 10:05', 0)")
                            db.execSQL("INSERT INTO comments (dish_id, author_name, rating, text, created_at, upvotes) VALUES (2, 'Maya', 3, 'Good, but a bit too salty for me.', '20-12-2025, 12:30', 0)")
                            db.execSQL("INSERT INTO comments (dish_id, author_name, rating, text, created_at, upvotes) VALUES (2, 'Noam', 4, 'Great crust, could use more basil.', '20-12-2025, 18:45', 0)")
                        }
                    })
                    .build()

                instance = dbInstance
                dbInstance
            }
    }
}
