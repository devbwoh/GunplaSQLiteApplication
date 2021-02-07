package kr.ac.kumoh.s20210000.gunplasqliteapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class GunplaViewModel(application: Application):
    AndroidViewModel(application) {

    private val mechanic: LiveData<List<Mechanic>>
    private val repository: GunplaRepository

    init {
        val dao = GunplaDatabase.getDatabase(application).gunplaDao()
        repository = GunplaRepository(dao)
        mechanic = repository.allMechanic.asLiveData()
    }

    fun getAllMechanic(): LiveData<List<Mechanic>> = mechanic

    fun getMechanic(i: Int) = mechanic.value?.get(i)
    fun getSize() = mechanic.value?.size

    fun insert(mechanic: Mechanic) = viewModelScope.launch {
        repository.insert(mechanic)
    }
}