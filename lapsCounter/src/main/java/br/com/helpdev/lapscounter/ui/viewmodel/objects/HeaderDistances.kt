package br.com.helpdev.lapscounter.ui.viewmodel.objects

import androidx.lifecycle.MutableLiveData

class HeaderDistances {
    val lapDistance = MutableLiveData<String>()
    val travelledDistance = MutableLiveData<String>()
    val pace = MutableLiveData<String>()
}