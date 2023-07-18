package com.frami.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.frami.data.model.appconfig.AppConfig
import com.frami.data.model.home.ActivityTypes
import com.frami.data.model.lookup.*
import com.frami.data.model.user.User
import com.frami.data.model.wearable.WearableData
import com.frami.utils.AppConstants
import io.reactivex.Single

class Daos {
    @Dao
    abstract class BaseDao<T> {
        @Insert
        abstract fun create(obj: T): Long

        @Insert
        abstract fun create(objs: Collection<T>): Single<List<Long>>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        abstract fun createOrReplace(obj: T)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        abstract fun createOrReplace(obj: Collection<T>)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        abstract fun createOrReplaceAll(obj: Collection<T>): Single<List<Long>>

        @Insert
        protected abstract fun insert(obj: T): Long

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        protected abstract fun insertIgnoringConflict(obj: T): Long

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        abstract fun insertIgnoringConflict(obj: Collection<T>): Single<List<Long>>

        @Update
        abstract fun update(obj: T)

        @Update
        abstract fun update(obj: List<T>)

        @Delete
        abstract fun delete(obj: T): Int

        @Delete
        abstract fun delete(objs: Collection<T>)

        @Transaction
        open fun createOrUpdate(obj: T) {
            val id = insertIgnoringConflict(obj)
            if (id == -1L) {
                update(obj)
            }
        }
    }

    @Dao
    abstract class AppConfigDao : BaseDao<AppConfig>() {
        @Query("SELECT * FROM AppConfig")
        abstract fun getData(): Single<AppConfig>
    }

    @Dao
    abstract class CountryDao : BaseDao<CountryData>() {
        @Query("SELECT * FROM Country ORDER BY name ASC")
        abstract fun getAll(): Single<List<CountryData>>

        @Query("SELECT COUNT(id) FROM Country")
        abstract fun count(): Single<Int>

        @Query("SELECT * FROM Country ORDER BY name ASC")
        abstract fun getAllLiveData(): LiveData<List<CountryData>>

        @Query("DELETE FROM Country")
        abstract fun deleteAll()
    }

    @Dao
    abstract class UserDao : BaseDao<User>() {
        @Query("SELECT * FROM User ORDER BY userId DESC LIMIT 1")
        abstract fun getById(): User?

        @Query("SELECT * FROM User WHERE userId = :userId")
        abstract fun getByUserId(userId: String): User?

        @Query("SELECT * FROM User ORDER BY userId ASC")
        abstract fun getAll(): Single<List<User>>

        @Query("SELECT COUNT(id) FROM User")
        abstract fun count(): Single<Int>

        @Query("SELECT * FROM User ORDER BY userId ASC")
        abstract fun getAllLiveData(): LiveData<List<User>>

        @Query("SELECT * FROM user ORDER BY userId DESC LIMIT 1")
        abstract fun getLiveData(): LiveData<User>

        @Query("UPDATE user SET isDeviceConnected = 1 WHERE userId = :userId")
        abstract fun updateUserDeviceConnected(
            userId: String
        ): Single<Int>

        @Query("UPDATE user SET workEmailAddress = :workEmail WHERE userId = :userId")
        abstract fun updateWorkEmail(
            userId: String,
            workEmail: String,
        ): Single<Int>

        @Query("UPDATE user SET emailAddress = :email WHERE userId = :userId")
        abstract fun updateEmail(
            userId: String,
            email: String
        ): Single<Int>

        @Query("DELETE FROM User")
        abstract fun deleteAll()
    }

    @Dao
    abstract class WearableDao : BaseDao<WearableData>() {
        @Query("UPDATE WearableData SET oAuthToken = :oAuthToken, oAuthVerifier = :oAuthVerifier WHERE name = :name")
        abstract fun updateOAuthTokenAndVerifier(
            oAuthToken: String,
            oAuthVerifier: String,
            name: String
        ): Single<Int>

        @Query("UPDATE WearableData SET oAuthTokenSecret = :oAuthTokenSecret WHERE name = :name")
        abstract fun updateOAuthTokenSecret(
            oAuthTokenSecret: String,
            name: String
        ): Single<Int>

        @Query("SELECT * FROM WearableData WHERE name = :name ORDER BY name ASC")
        abstract fun loadByName(name: String): Single<WearableData>

        @Query("SELECT COUNT(id) FROM WearableData")
        abstract fun count(): Single<Int>

        @Query("SELECT * FROM WearableData ORDER BY name ASC")
        abstract fun getAllLiveData(): LiveData<List<WearableData>>

        @Query("SELECT * FROM WearableData ORDER BY id ASC")
        abstract fun getAll(): Single<List<WearableData>>

        @Query("DELETE FROM WearableData")
        abstract fun deleteAll()
    }

    @Dao
    abstract class ActivityTypesDao : BaseDao<ActivityTypes>() {
        @Query("SELECT * FROM ActivityTypes")
        abstract fun getAll(): List<ActivityTypes>

        @Query("SELECT * FROM ActivityTypes WHERE `key` = '${AppConstants.KEYS.ALL}'")
        abstract fun getActivityTypeAll(): ActivityTypes

        @Query("SELECT COUNT(`key`) FROM ActivityTypes")
        abstract fun count(): Single<Int>

        @Query("DELETE FROM ActivityTypes")
        abstract fun deleteAll()
    }

    @Dao
    abstract class DistanceTypesDao : BaseDao<DistanceUnits>() {
        @Query("SELECT * FROM DistanceTypes")
        abstract fun getAll(): List<DistanceUnits>

        @Query("SELECT COUNT(`key`) FROM DistanceTypes")
        abstract fun count(): Single<Int>

        @Query("DELETE FROM DistanceTypes")
        abstract fun deleteAll()
    }

    @Dao
    abstract class AvgPaceTypesDao : BaseDao<AvgPaceUnits>() {
        @Query("SELECT * FROM AvgPaceTypes")
        abstract fun getAll(): List<AvgPaceUnits>

        @Query("SELECT COUNT(`key`) FROM AvgPaceTypes")
        abstract fun count(): Single<Int>

        @Query("DELETE FROM AvgPaceTypes")
        abstract fun deleteAll()
    }

    @Dao
    abstract class AnalysisTypesDao : BaseDao<AnalysisTypes>() {
        @Query("SELECT * FROM AnalysisTypes")
        abstract fun getAll(): List<AnalysisTypes>

        @Query("SELECT COUNT(`key`) FROM AnalysisTypes")
        abstract fun count(): Single<Int>

        @Query("DELETE FROM AnalysisTypes")
        abstract fun deleteAll()
    }

    @Dao
    abstract class ActivityTitleDao : BaseDao<ActivityTitle>() {
        @Query("SELECT * FROM ActivityTitle")
        abstract fun getAll(): List<ActivityTitle>

        @Query("SELECT COUNT(`key`) FROM ActivityTitle")
        abstract fun count(): Single<Int>

        @Query("DELETE FROM ActivityTitle")
        abstract fun deleteAll()
    }
}
