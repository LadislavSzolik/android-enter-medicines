package com.example.captureinvoice

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface MedicineDao {

    //TODO: add filtering
    @Query("SELECT * FROM medicine")
    fun medicines(): PagingSource<Int,Medicine>

    @Insert
    suspend fun insert(medicine: Medicine)

    @Query("DELETE FROM medicine")
    fun deleteAll()
}
