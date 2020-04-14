package com.example.themovie.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Movie::class, FavMovies::class], version = 4)
@TypeConverters(GenresConverter::class)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun favDao(): FavDao

    companion object {
        @JvmField
        val MIGRATION_1_2 = Migration1To2()
        val MIGRATION_2_3 = Migration2To3()
        val MIGRATION_3_4 = Migration3To4()

        var INSTANCE: MovieDatabase? = null
        fun getDatabase(context: Context): MovieDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "app_database.db"
                ).addMigrations(MovieDatabase.MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4).build()
            }
            return INSTANCE!!
        }
    }

    class Migration1To2 : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "ALTER TABLE movie_table "
                        + "ADD COLUMN ListData TEXT"
            )
        }
    }

    class Migration2To3 : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "ALTER TABLE movie_table "
                        + "ADD COLUMN ListData TEXT"
            )
        }
    }
    class Migration3To4 : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "ALTER TABLE movie_table "
                        + "ADD COLUMN ListData TEXT"
            )
        }
    }
}