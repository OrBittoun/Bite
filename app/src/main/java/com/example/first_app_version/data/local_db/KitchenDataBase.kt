package com.example.first_app_version.data.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.first_app_version.R
import com.example.first_app_version.data.models.Comment
import com.example.first_app_version.data.models.Dish
import com.example.first_app_version.data.models.DishType
import com.example.first_app_version.data.models.Kitchen

@Database(
    entities = [Kitchen::class, DishType::class, Dish::class, Comment::class],
    version = 7, // bump to add sample comments for "Pasta Alfredo"
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

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE dishes ADD COLUMN restaurantName TEXT NOT NULL DEFAULT ''")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS comments (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        dish_id INTEGER NOT NULL,
                        author_name TEXT NOT NULL,
                        rating INTEGER NOT NULL,
                        text TEXT NOT NULL,
                        created_at TEXT NOT NULL,
                        upvotes INTEGER NOT NULL,
                        FOREIGN KEY(dish_id) REFERENCES dishes(id) ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_comments_dish_id ON comments(dish_id)")
            }
        }

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS comments_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        dish_id INTEGER NOT NULL,
                        author_name TEXT NOT NULL,
                        rating INTEGER NOT NULL,
                        text TEXT NOT NULL,
                        created_at TEXT NOT NULL,
                        upvotes INTEGER NOT NULL,
                        FOREIGN KEY(dish_id) REFERENCES dishes(id) ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO comments_new (id, dish_id, author_name, rating, text, created_at, upvotes)
                    SELECT id, dish_id, author_name, rating, text, created_at, upvotes
                    FROM comments
                    """.trimIndent()
                )
                db.execSQL("DROP TABLE comments")
                db.execSQL("ALTER TABLE comments_new RENAME TO comments")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_comments_dish_id ON comments(dish_id)")
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_comments_dish_id_author_name ON comments(dish_id, author_name)")
            }
        }

        // 6 -> 7: insert 3 sample comments for "Pasta Alfredo" (id-agnostic via name lookup)
        private val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Only seed if dish exists and there are currently no comments by these authors
                // Authors: "Giulia", "Marco", "Sofia" with different ratings
                // created_at format matches your display: "dd-MM-yyyy, HH:mm"
                db.execSQL(
                    """
                    INSERT INTO comments (dish_id, author_name, rating, text, created_at, upvotes)
                    SELECT
                        (SELECT d.id FROM dishes d
                         JOIN dish_types dt ON dt.id = d.dish_type_id
                         JOIN kitchens k ON k.id = dt.kitchen_id
                         WHERE d.name = 'Pasta Alfredo' AND dt.name = 'Pasta' AND k.name = 'Italian'),
                        'Giulia', 5, 'Absolutely delicious and creamy!', '24-12-2025, 15:00', 0
                    WHERE NOT EXISTS (
                        SELECT 1 FROM comments c
                        WHERE c.author_name = 'Giulia'
                          AND c.dish_id = (SELECT d.id FROM dishes d
                                           JOIN dish_types dt ON dt.id = d.dish_type_id
                                           JOIN kitchens k ON k.id = dt.kitchen_id
                                           WHERE d.name = 'Pasta Alfredo' AND dt.name = 'Pasta' AND k.name = 'Italian')
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO comments (dish_id, author_name, rating, text, created_at, upvotes)
                    SELECT
                        (SELECT d.id FROM dishes d
                         JOIN dish_types dt ON dt.id = d.dish_type_id
                         JOIN kitchens k ON k.id = dt.kitchen_id
                         WHERE d.name = 'Pasta Alfredo' AND dt.name = 'Pasta' AND k.name = 'Italian'),
                        'Marco', 3, 'Good, but a bit heavy for my taste.', '24-12-2025, 15:05', 0
                    WHERE NOT EXISTS (
                        SELECT 1 FROM comments c
                        WHERE c.author_name = 'Marco'
                          AND c.dish_id = (SELECT d.id FROM dishes d
                                           JOIN dish_types dt ON dt.id = d.dish_type_id
                                           JOIN kitchens k ON k.id = dt.kitchen_id
                                           WHERE d.name = 'Pasta Alfredo' AND dt.name = 'Pasta' AND k.name = 'Italian')
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO comments (dish_id, author_name, rating, text, created_at, upvotes)
                    SELECT
                        (SELECT d.id FROM dishes d
                         JOIN dish_types dt ON dt.id = d.dish_type_id
                         JOIN kitchens k ON k.id = dt.kitchen_id
                         WHERE d.name = 'Pasta Alfredo' AND dt.name = 'Pasta' AND k.name = 'Italian'),
                        'Sofia', 4, 'Tasty and comforting, loved the sauce!', '24-12-2025, 15:10', 0
                    WHERE NOT EXISTS (
                        SELECT 1 FROM comments c
                        WHERE c.author_name = 'Sofia'
                          AND c.dish_id = (SELECT d.id FROM dishes d
                                           JOIN dish_types dt ON dt.id = d.dish_type_id
                                           JOIN kitchens k ON k.id = dt.kitchen_id
                                           WHERE d.name = 'Pasta Alfredo' AND dt.name = 'Pasta' AND k.name = 'Italian')
                    )
                    """.trimIndent()
                )
            }
        }

        fun getDataBase(context: Context): KitchenDataBase =
            instance ?: synchronized(this) {
                val db = Room.databaseBuilder(
                    context.applicationContext,
                    KitchenDataBase::class.java,
                    "bite_db"
                )
                    .allowMainThreadQueries()
                    .addMigrations(MIGRATION_3_4, MIGRATION_4_5, MIGRATION_6_7)
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Fresh install seeds (kept as you had). Optionally also insert the sample comments here:
                            db.execSQL(
                                """
                                INSERT INTO comments (dish_id, author_name, rating, text, created_at, upvotes)
                                SELECT
                                    (SELECT d.id FROM dishes d
                                     JOIN dish_types dt ON dt.id = d.dish_type_id
                                     JOIN kitchens k ON k.id = dt.kitchen_id
                                     WHERE d.name = 'Pasta Alfredo' AND dt.name = 'Pasta' AND k.name = 'Italian'),
                                    'Giulia', 5, 'Absolutely delicious and creamy!', '24-12-2025, 15:00', 0
                                WHERE NOT EXISTS (
                                    SELECT 1 FROM comments c
                                    WHERE c.author_name = 'Giulia'
                                      AND c.dish_id = (SELECT d.id FROM dishes d
                                                       JOIN dish_types dt ON dt.id = d.dish_type_id
                                                       JOIN kitchens k ON k.id = dt.kitchen_id
                                                       WHERE d.name = 'Pasta Alfredo' AND dt.name = 'Pasta' AND k.name = 'Italian')
                                )
                                """.trimIndent()
                            )
                            db.execSQL(
                                """
                                INSERT INTO comments (dish_id, author_name, rating, text, created_at, upvotes)
                                SELECT
                                    (SELECT d.id FROM dishes d
                                     JOIN dish_types dt ON dt.id = d.dish_type_id
                                     JOIN kitchens k ON k.id = dt.kitchen_id
                                     WHERE d.name = 'Pasta Alfredo' AND dt.name = 'Pasta' AND k.name = 'Italian'),
                                    'Marco', 3, 'Good, but a bit heavy for my taste.', '24-12-2025, 15:05', 0
                                WHERE NOT EXISTS (
                                    SELECT 1 FROM comments c
                                    WHERE c.author_name = 'Marco'
                                      AND c.dish_id = (SELECT d.id FROM dishes d
                                                       JOIN dish_types dt ON dt.id = d.dish_type_id
                                                       JOIN kitchens k ON k.id = dt.kitchen_id
                                                       WHERE d.name = 'Pasta Alfredo' AND dt.name = 'Pasta' AND k.name = 'Italian')
                                )
                                """.trimIndent()
                            )
                            db.execSQL(
                                """
                                INSERT INTO comments (dish_id, author_name, rating, text, created_at, upvotes)
                                SELECT
                                    (SELECT d.id FROM dishes d
                                     JOIN dish_types dt ON dt.id = d.dish_type_id
                                     JOIN kitchens k ON k.id = dt.kitchen_id
                                     WHERE d.name = 'Pasta Alfredo' AND dt.name = 'Pasta' AND k.name = 'Italian'),
                                    'Sofia', 4, 'Tasty and comforting, loved the sauce!', '24-12-2025, 15:10', 0
                                WHERE NOT EXISTS (
                                    SELECT 1 FROM comments c
                                    WHERE c.author_name = 'Sofia'
                                      AND c.dish_id = (SELECT d.id FROM dishes d
                                                       JOIN dish_types dt ON dt.id = d.dish_type_id
                                                       JOIN kitchens k ON k.id = dt.kitchen_id
                                                       WHERE d.name = 'Pasta Alfredo' AND dt.name = 'Pasta' AND k.name = 'Italian')
                                )
                                """.trimIndent()
                            )
                        }
                    })
                    .build()
                instance = db
                db
            }
    }
}