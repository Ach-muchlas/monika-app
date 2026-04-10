package com.sss.monikaapps.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sss.monikaapps.common.db.entity.PhotoEntity
import com.sss.monikaapps.common.repository.photo.PhotoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PhotoViewModel(private val repository: PhotoRepository) : ViewModel() {

    fun observePhotos(parentId: String, parentType: String): StateFlow<List<PhotoEntity>> {
        return repository.observePhotos(parentId, parentType)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun observerPhotosInvoice(parentId: String, parentType: String, parentFeature: Int)  : StateFlow<List<PhotoEntity>> {
        return repository.observePhotosInvoice(parentId, parentType, parentFeature)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun addPhoto(parentId: String, parentFeature: Int, parentType: String, path: String) {
        viewModelScope.launch {
            repository.addPhoto(
                parentId = parentId,
                parentType = parentType,
                parentFeature = parentFeature,
                path = path
            )
        }
    }

    fun deletePhoto(photo: PhotoEntity) {
        viewModelScope.launch {
            repository.deletePhoto(photo)
        }
    }
}
