package com.udacity.shoestore.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.shoestore.models.Shoe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * @author Komi Donon
 * @since 4/10/2023
 */
class MainViewModel : ViewModel() {

    private var _isNewShoeAdded = MutableLiveData(false)
    val isNewShoeAdded: LiveData<Boolean>
        get() = _isNewShoeAdded

    private var _isShoeAlreadyExist = MutableLiveData(false)
    val isShoeAlreadyExist: LiveData<Boolean>
        get() = _isShoeAlreadyExist

    var shoe: MutableLiveData<Shoe> = MutableLiveData<Shoe>(Shoe("", 0.0, "", "", mutableListOf()))

    private val _shoeList = MutableStateFlow(Shoe.defaultShoeList)
    val shoeList = _shoeList.asStateFlow()

    private val _isEmptyInputFieldFound = MutableStateFlow(false)
    val isEmptyInputFieldFound = _isEmptyInputFieldFound.asStateFlow()

    fun onSavedClicked(shoeModel: Shoe) {
        if (shoeModel.name.isBlank() && shoeModel.company.isBlank() && shoeModel.size <= 0.0 && shoeModel.description.isBlank()) {
            _isEmptyInputFieldFound.value = true
        } else if (_shoeList.value.find { shoe -> shoe.name == shoeModel.name && shoe.size == shoeModel.size } != null) {
            _isShoeAlreadyExist.value = true
        } else {
            _shoeList.value.add(shoeModel)
            _isNewShoeAdded.value = true
        }
    }

    fun resetIsNewShoeAddedState() {
        _isNewShoeAdded.value = false
        _isShoeAlreadyExist.value = false
        shoe.value = Shoe("", 0.0, "", "", mutableListOf())
        _isEmptyInputFieldFound.value = false
    }

    fun resetIsEmptyInputFieldFound() {
        _isEmptyInputFieldFound.value = false
    }
}
