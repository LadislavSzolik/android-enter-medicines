package com.example.captureinvoice

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Medicine::class],version = 1, exportSchema = true)
abstract class MedicineRoomDatabase: RoomDatabase() {

    abstract fun medicineDao() :MedicineDao

    private class MedicineDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let {database ->
                scope.launch {
                    populateDatabase(database.medicineDao())
                }
            }
        }

        suspend fun populateDatabase(medicineDao: MedicineDao)
        {
            // TODO: Remove this as we preload the data from the file
            medicineDao.deleteAll()
            var medicine = Medicine("Medinince 1")
            medicineDao.insert(medicine)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE:MedicineRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): MedicineRoomDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder (
                    context.applicationContext,
                    MedicineRoomDatabase::class.java,
                    //TODO: fix db number
                    "medicine_database_2"
                ).createFromAsset("database.db").build()
                INSTANCE = instance
                instance
            }
        }
    }

}



