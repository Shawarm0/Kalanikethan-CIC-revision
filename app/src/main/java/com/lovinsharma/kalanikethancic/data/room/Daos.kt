package com.lovinsharma.kalanikethancic.data.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.lovinsharma.kalanikethancic.data.room.models.Event
import com.lovinsharma.kalanikethancic.data.room.models.Family
import com.lovinsharma.kalanikethancic.data.room.models.Parents
import com.lovinsharma.kalanikethancic.data.room.models.Students
import kotlinx.coroutines.flow.Flow


@Dao
interface FamilyDao{
    @Upsert //This creates a new family or updates existing family if the id matches
    suspend fun insert(family: Family): Long

    @Update(onConflict = OnConflictStrategy.REPLACE) //This updates the family
    suspend fun update(family: Family)

    @Delete
    suspend fun delete(family: Family)

    @Query("SELECT MAX(family_ID) FROM family_table")
    fun getMaxFamilyId():Flow<Long>


}

@Dao
interface EventDao {


    @Query("SELECT * FROM event WHERE student_ID = :studentId AND is_Open = 1")
    suspend fun findOpenEventByStudentId(studentId: Int): Event?

    @Upsert
    suspend fun insert(event: Event)

    @Query("SELECT * FROM event WHERE is_absent = 0")
    fun history():Flow<List<Event>>


}


@Dao
interface StudentDao{
    @Upsert
    suspend fun insert(students: Students)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(students: Students)

    @Delete
    suspend fun delete(students: Students)

    @Query("SELECT * FROM students_table")
    fun getAllStudents():Flow<List<Students>>


    @Query("SELECT * FROM students_table WHERE signed_in = 0")
    fun getPagedStudents(): PagingSource<Int, Students>



    @Query("SELECT * FROM students_table WHERE signed_in = 1")
    fun getSignedInStudents(): Flow<List<Students>> // Students who are signed in


    @Query("SELECT * FROM students_table WHERE signed_in = 0")
    fun getUnsignedStudents(): Flow<List<Students>> // Students who are not signed in


    @Query("UPDATE students_table SET signed_in = :status WHERE student_ID = :studentID")
    suspend fun updateSignInStatus(studentID: Int, status: Boolean) // Update sign-in status

    // Modify the query to use the LIKE operator for dynamic search
    @Query("SELECT * FROM students_table WHERE student_name LIKE '%' || :studentName || '%' AND signed_in == 0")
    fun searchStudentsByName(studentName: String): Flow<List<Students>>
}

@Dao
interface ParentsDao{
    @Upsert
    suspend fun insert(parents: Parents)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(parents: Parents)

    @Query("SELECT * FROM parents_table WHERE family_ID = :familyId")
    suspend fun getParentsByFamilyId(familyId: Int): List<Parents>

    @Query("SELECT * FROM parents_table")
    fun getAllParents():Flow<List<Parents>>

    @Delete
    suspend fun delete(parents: Parents)
}


