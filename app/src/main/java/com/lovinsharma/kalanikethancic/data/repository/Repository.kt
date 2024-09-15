package com.lovinsharma.kalanikethancic.data.repository

import androidx.lifecycle.LiveData
import com.lovinsharma.kalanikethancic.data.room.FamilyDao
import com.lovinsharma.kalanikethancic.data.room.ParentsDao
import com.lovinsharma.kalanikethancic.data.room.StudentDao
import com.lovinsharma.kalanikethancic.data.room.models.Family
import com.lovinsharma.kalanikethancic.data.room.models.Parents
import com.lovinsharma.kalanikethancic.data.room.models.Students
import kotlinx.coroutines.flow.Flow

class Repository(
    private val familyDao: FamilyDao,
    private val studentDao: StudentDao,
    private val parentsDao: ParentsDao
) {

    // Student-related operations
    val students: Flow<List<Students>> = studentDao.getAllStudents()
    val signedInStudents: Flow<List<Students>> = studentDao.getSignedInStudents()
    val unsignedStudents: Flow<List<Students>> = studentDao.getUnsignedStudents()
    val getLastFamily: Flow<Long> = familyDao.getMaxFamilyId()

    fun searchStudentsByName(studentName: String): Flow<List<Students>> = studentDao.searchStudentsByName(studentName)

    suspend fun updateSignInStatus(id: Int, status: Boolean) = studentDao.updateSignInStatus(id, status)

    suspend fun insertStudent(student: Students) = studentDao.insert(student)

    suspend fun updateStudent(student: Students) = studentDao.update(student)

    suspend fun deleteStudent(student: Students) = studentDao.delete(student)



    // Family-related operations
    suspend fun insertFamily(family: Family) {
        familyDao.insert(family)
    }

    suspend fun updateFamily(family: Family) = familyDao.update(family)

    suspend fun deleteFamily(family: Family) = familyDao.delete(family)



    // Parent-related operations
    suspend fun insertParent(parent: Parents) = parentsDao.insert(parent)

    suspend fun updateParent(parent: Parents) = parentsDao.update(parent)

    suspend fun deleteParent(parent: Parents) = parentsDao.delete(parent)
}
