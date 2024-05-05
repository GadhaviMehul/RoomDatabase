package com.example.roomdatabase42024

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [UserData::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        private var INSTANCES: UserDatabase? = null
        fun getUserDatabase(): UserDatabase {
            val temp = INSTANCES
            if (temp != null) {
                return temp
            }
            synchronized(this) {
                val instances = Room.databaseBuilder(
                    App.getApplicationContext(),
                    UserDatabase::class.java,
                    "UserDatabase"
                ).fallbackToDestructiveMigration().build()
                INSTANCES = instances
                return instances
            }

        }
    }
}