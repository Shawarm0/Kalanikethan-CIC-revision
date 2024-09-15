package com.lovinsharma.kalanikethancic.Viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lovinsharma.kalanikethancic.data.repository.Repository
import com.lovinsharma.kalanikethancic.data.room.models.Family
import com.lovinsharma.kalanikethancic.data.room.models.Parents
import com.lovinsharma.kalanikethancic.data.room.models.Students
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MyViewModel(
    private val repository: Repository
) : ViewModel() {


    val lastFamily = repository.getLastFamily.asLiveData()

    val students = repository.students.asLiveData()
    val signedInStudents = repository.signedInStudents.asLiveData()
    val unsignedStudents = repository.unsignedStudents.asLiveData()

    fun searchStudents(name: String) = repository.searchStudentsByName(name).asLiveData()

    fun updateStudentStatus(id: Int, status: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateSignInStatus(id, status)
        }
    }

    fun addStudent(student: Students) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertStudent(student)
        }
    }

    fun modifyStudent(student: Students) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateStudent(student)
        }
    }



    fun removeStudent(student: Students) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteStudent(student)
        }
    }

    fun addFamily(family: Family,) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertFamily(family)
        }
    }



    fun modifyFamily(family: Family) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateFamily(family)
        }
    }

    fun removeFamily(family: Family) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFamily(family)
        }
    }

    fun addParent(parent: Parents) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertParent(parent)
        }
    }

    fun modifyParent(parent: Parents) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateParent(parent)
        }
    }

    fun removeParent(parent: Parents) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteParent(parent)
        }
    }
}
