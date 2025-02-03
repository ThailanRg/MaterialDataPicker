package br.com.datapikercustom.sample1.presentation.viewmodel

sealed interface  MaterialEvent {

    data class ReceiverDateSelected(val date:String):MaterialEvent

}