package com.example.medplus_user.presentation.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medplus_user.domain.models.Category
import com.example.medplus_user.domain.useCases.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMedicinesUseCase: GetCategoriesUseCase
) : ViewModel() {
//    private val _loading = MutableLiveData<Boolean>()
//    val loading: LiveData<Boolean> = _loading
    val categories :StateFlow<List<Category>> = getMedicinesUseCase
        .invoke()
        .stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000L),initialValue = emptyList())
}