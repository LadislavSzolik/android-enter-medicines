package com.example.captureinvoice

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class CapturInvoiceApplication: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy {MedicineRoomDatabase.getDatabase(this,applicationScope)}
    val repository by lazy { MedicineRepository(database.medicineDao())}
}
