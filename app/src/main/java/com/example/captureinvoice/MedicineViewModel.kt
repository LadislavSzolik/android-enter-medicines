package com.example.captureinvoice


import androidx.lifecycle.*

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.filter
import kotlinx.coroutines.flow.combine

import java.lang.IllegalArgumentException

class MedicineViewModel(private val repository: MedicineRepository):ViewModel() {

    val newMedicines: MutableList<String> = mutableListOf()

    private val _invoiceNr = MutableLiveData("")
    val invoiceNr: LiveData<String> = _invoiceNr
    fun onInvoiceNrChange(newInvoiceNr: String) {
        _invoiceNr.value = newInvoiceNr
    }


    private val _selectedMedicine = MutableLiveData("")
    val selectedMedicine: LiveData<String> = _selectedMedicine
    fun onMedicineChange(newSelectedMedicine: String) {
        _selectedMedicine.value = newSelectedMedicine
    }



    // Search text in the list of all medicine name
    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> = _searchQuery
    fun onSearchQueryChange(newSearchQuery: String) {
        _searchQuery.value = newSearchQuery
    }

    // Logic to read from db and filter based on search query
    val pagingSourceFactory = { repository.accessDao.medicines()}
    val medicineFlow = Pager(
        PagingConfig(pageSize = 50),
    ){
        pagingSourceFactory()
    }.flow.cachedIn(viewModelScope).combine(searchQuery.asFlow()) {
        pagingData, searchQuery ->
        pagingData.filter { it.string.contains(searchQuery, ignoreCase = true) }
    }
}

class MedicineViewModelFactory(private val repository: MedicineRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) : T {
        if(modelClass.isAssignableFrom(MedicineViewModel::class.java)) {
            return MedicineViewModel(repository) as T
        }
        throw  IllegalArgumentException("Unknown View Model class")
    }
}
