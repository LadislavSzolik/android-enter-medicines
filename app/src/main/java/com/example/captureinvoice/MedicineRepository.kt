package com.example.captureinvoice

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow

class MedicineRepository(private val medicineDao: MedicineDao) {

    //val medicines: PagingSource<Int, Medicine> = medicineDao.medicines()

    val accessDao:MedicineDao = medicineDao

    @WorkerThread
    suspend fun insert(medicine: Medicine) {
        medicineDao.insert(medicine = medicine)
    }
}
