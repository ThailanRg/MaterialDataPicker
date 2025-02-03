package br.com.datapikercustom.sample1.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MaterialDataPikerViewModel : ViewModel() {

    private var _state = MutableLiveData<String>()
    val state = _state

    fun onEvent(event:MaterialEvent){
        when(event){
            is MaterialEvent.ReceiverDateSelected -> {
                state.postValue(event.date)
            }
        }
    }


    data class MaterialDataPikerState(
        val dateSelected:String
    )

}

