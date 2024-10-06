package com.lovinsharma.kalanikethancic.data.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "family_table")
data class Family(
    @ColumnInfo(name = "family_ID") @PrimaryKey(autoGenerate = true) val familyID: Int=0,
    @ColumnInfo(name = "family_name") val familyname: String,
    @ColumnInfo(name = "paymentID") var paymentID: String,
)

@Entity(
    tableName = "students_table",
    foreignKeys = [ForeignKey(
        entity = Family::class,
        parentColumns = ["family_ID"],
        childColumns = ["family_ID"],
        onDelete = ForeignKey.CASCADE
        )]

)
data class Students(
    @ColumnInfo(name = "student_ID") @PrimaryKey(autoGenerate = true) val studentID: Int=0,
    @ColumnInfo(name = "student_name") var studentName: String,
    @ColumnInfo(name = "student_number") var studentNumber: String,
    @ColumnInfo(name = "student_dob") var birthdate: String,
    @ColumnInfo(name = "can_walk_alone") var canWalkAlone: Boolean = false,
    @ColumnInfo(name = "signed_in") val signedIn: Boolean = false,
    @ColumnInfo(name = "family_ID") val familyIDfk: Int,
)



@Entity(
    tableName = "event",
    foreignKeys = [
        ForeignKey(
            entity = Students::class,
            parentColumns = ["student_ID"],
            childColumns = ["student_ID"],
            onDelete = ForeignKey.CASCADE // Optional: Cascades deletion from Students to Event if a student is deleted
        )
    ],
    indices = [Index(value = ["student_ID"])] // Helps with faster lookups for foreign key
)

data class Event(
    @ColumnInfo(name = "event_ID") @PrimaryKey(autoGenerate = true) val eventId: Int = 0,
    @ColumnInfo(name = "day") var day: String,
    @ColumnInfo(name = "student_name") var studentName: String,
    @ColumnInfo(name = "sign_in_time") var signIn: String,
    @ColumnInfo(name = "sign_out_time") var signOut: String,
    @ColumnInfo(name = "student_ID") var studentIDfk: Int,
    @ColumnInfo(name = "is_Open") var isOpen: Boolean = false,
    @ColumnInfo(name = "is_absent") var isAbsent: Boolean = false,
    @ColumnInfo(name = "reason_For_Absence") var reasonForAbsence: String="",
)


@Entity(
    tableName = "parents_table",
    foreignKeys = [ForeignKey(
        entity = Family::class,
        parentColumns = ["family_ID"],
        childColumns = ["family_ID"],
        onDelete = ForeignKey.CASCADE
    )]

)
data class Parents(
    @ColumnInfo(name = "parent_ID") @PrimaryKey(autoGenerate = true) val parentID: Int=0,
    @ColumnInfo(name = "parent_name") var parentName: String,
    @ColumnInfo(name = "parent_number") var parentNumber: String,
    @ColumnInfo(name = "parent_email") var parentEmail: String,
    @ColumnInfo(name = "payment_date") var paymentDate: String,
    @ColumnInfo(name = "family_ID") val familyIDfk: Int,
)

