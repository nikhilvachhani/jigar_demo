package com.frami.data.local.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.frami.FramiApp
import com.frami.data.local.db.Daos.*
import com.frami.data.model.appconfig.AppConfig
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.lookup.*
import com.frami.data.model.user.User
import com.frami.data.model.wearable.WearableData
import com.frami.utils.AppConstants

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
    version = 10,
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
        @get:Synchronized
        val db: AppDatabase by lazy {
            Room.databaseBuilder(FramiApp.appContext, AppDatabase::class.java, AppConstants.DB_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
