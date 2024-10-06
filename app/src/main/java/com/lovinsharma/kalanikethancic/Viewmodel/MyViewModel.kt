package com.lovinsharma.kalanikethancic.Viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.lovinsharma.kalanikethancic.data.repository.Repository
import com.lovinsharma.kalanikethancic.data.room.StudentDao
import com.lovinsharma.kalanikethancic.data.room.models.Event
import com.lovinsharma.kalanikethancic.data.room.models.Family
import com.lovinsharma.kalanikethancic.data.room.models.Parents
import com.lovinsharma.kalanikethancic.data.room.models.Students
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class MyViewModel(
    private val repository: Repository,
) : ViewModel() {

    val history = repository.history.asLiveData()

    val lastFamily = repository.getLastFamily.asLiveData()

    val students = repository.students.asLiveData()
    val signedInStudents = repository.signedInStudents.asLiveData()
    val pagedUnsignedStudents = Pager(PagingConfig(pageSize = 20)) {
        repository.getPagedUnsignedStudents()
    }.flow.cachedIn(viewModelScope)





    val parents = repository.parents.asLiveData()


    fun addHistory(event: Event) {
        viewModelScope.launch {
            repository.insertHistory(event)
        }
    }


    fun upsertEvent(event: Event) {
        viewModelScope.launch {
            repository.upsertEvent(event)
        }
    }



    fun updateSignOutTime(studentIDfk: Int, newSignOutTime: String) {
        viewModelScope.launch {
            // Find the existing event
            val existingEvent = repository.findOpenEventByStudentId(studentIDfk)
            if (existingEvent != null) {
                // Create a new Event object based on the existing one but with the updated signOut time and isOpen set to false
                val updatedEvent = existingEvent.copy(
                    signOut = newSignOutTime,
                    isOpen = false  // Set isOpen to false
                )
                repository.upsertEvent(updatedEvent)
            }
        }

    }

    // Function to return LiveData for searched students
    fun searchStudents(query: String): LiveData<List<Students>> {
        return if (query.isEmpty()) {
            // If the search query is empty, return all students
            repository.getAllStudents().asLiveData()
        } else {
            // Otherwise, search by name
            repository.searchStudentsByName(query).asLiveData()
        }
    }
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

    fun getParentsByFamilyId(familyId: Int): LiveData<List<Parents>> {
        return liveData {
            val parents = repository.getParentsByFamilyId(familyId)
            emit(parents)
        }
    }

    fun removeParent(parent: Parents) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteParent(parent)
        }
    }
}
