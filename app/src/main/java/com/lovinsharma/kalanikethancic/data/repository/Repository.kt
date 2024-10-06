package com.lovinsharma.kalanikethancic.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.lovinsharma.kalanikethancic.data.room.EventDao
import com.lovinsharma.kalanikethancic.data.room.FamilyDao
import com.lovinsharma.kalanikethancic.data.room.ParentsDao
import com.lovinsharma.kalanikethancic.data.room.StudentDao
import com.lovinsharma.kalanikethancic.data.room.models.Event
import com.lovinsharma.kalanikethancic.data.room.models.Family
import com.lovinsharma.kalanikethancic.data.room.models.Parents
import com.lovinsharma.kalanikethancic.data.room.models.Students
import kotlinx.coroutines.flow.Flow

class Repository(
    private val familyDao: FamilyDao,
    private val studentDao: StudentDao,
    private val parentsDao: ParentsDao,
    private val eventDao: EventDao,
) {

    // Student-related operations
    val students: Flow<List<Students>> = studentDao.getAllStudents()
    val signedInStudents: Flow<List<Students>> = studentDao.getSignedInStudents()
    val unsignedStudents: Flow<List<Students>> = studentDao.getUnsignedStudents()
    val getLastFamily: Flow<Long> = familyDao.getMaxFamilyId()

    val history: Flow<List<Event>> = eventDao.history()

    fun getPagedUnsignedStudents(): PagingSource<Int, Students> {
        return studentDao.getPagedStudents()
    }


    val parents: Flow<List<Parents>> = parentsDao.getAllParents()

    fun getAllStudents(): Flow<List<Students>> {
        return studentDao.getAllStudents() // Assuming you have this DAO method
    }


    fun searchStudentsByName(studentName: String): Flow<List<Students>> {
        return studentDao.searchStudentsByName("%$studentName%")
    }

    suspend fun updateSignInStatus(id: Int, status: Boolean) = studentDao.updateSignInStatus(id, status)

    suspend fun insertStudent(student: Students) = studentDao.insert(student)

    suspend fun updateStudent(student: Students) = studentDao.update(student)

    suspend fun deleteStudent(student: Students) = studentDao.delete(student)

    suspend fun upsertEvent(event: Event) {
        // Check if an open event exists for the specific student
        val existingEvent = eventDao.findOpenEventByStudentId(event.studentIDfk)

        if (existingEvent != null) {
            // If an open event exists, update signOut and set isOpen to false
            val updatedEvent = existingEvent.copy(
                signOut = event.signOut, // Update signOut only
                isOpen = false // Set isOpen to false
            )
            eventDao.insert(updatedEvent)  // Upsert will handle the update
        } else {
            // No open event exists, so insert the new event
            eventDao.insert(event)
        }
    }


    suspend fun findOpenEventByStudentId(studentID: Int): Event? {
        return eventDao.findOpenEventByStudentId(studentID)
    }

    suspend fun insertHistory(event: Event)  = eventDao.insert(event)




    // Family-related operations
    suspend fun insertFamily(family: Family) {
        familyDao.insert(family)
    }

    suspend fun updateFamily(family: Family) = familyDao.update(family)

    suspend fun deleteFamily(family: Family) = familyDao.delete(family)



    // Parent-related operations
    suspend fun insertParent(parent: Parents) = parentsDao.insert(parent)

    suspend fun updateParent(parent: Parents) = parentsDao.update(parent)

    suspend fun getParentsByFamilyId(familyId: Int): List<Parents> {
        return parentsDao.getParentsByFamilyId(familyId)
    }


    suspend fun deleteParent(parent: Parents) = parentsDao.delete(parent)
}
