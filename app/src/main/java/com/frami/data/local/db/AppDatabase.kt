package com.frami.data.local.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.frami.FramiApp
import com.frami.data.local.db.AppDatabase.Companion.DATABASE_VERSION
import com.frami.data.local.db.Daos.*
import com.frami.data.model.appconfig.AppConfig
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.lookup.*
import com.frami.data.model.user.User
import com.frami.data.model.wearable.WearableData

@Database(
    entities = [
        AppConfig::class,
        User::class,
        CountryData::class,
        WearableData::class,
        ActivityTypes::class,
        DistanceUnits::class,
        AvgPaceUnits::class,
        AnalysisTypes::class,
        ActivityTitle::class,
    ],
    version = DATABASE_VERSION,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appConfigDao(): AppConfigDao
    abstract fun countryDao(): CountryDao
    abstract fun userDao(): UserDao
    abstract fun wearableDao(): WearableDao
    abstract fun activityTypesDao(): ActivityTypesDao
    abstract fun distanceTypesDao(): DistanceTypesDao
    abstract fun avgPaceTypesDao(): AvgPaceTypesDao
    abstract fun analysisTypesDao(): AnalysisTypesDao
    abstract fun activityTitleDao(): ActivityTitleDao

    companion object {
        const val DATABASE_NAME = "frami.db"
        const val DATABASE_VERSION = 8

        @get:Synchronized
        val db: AppDatabase by lazy {
            Room.databaseBuilder(FramiApp.appContext, AppDatabase::class.java, DATABASE_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}