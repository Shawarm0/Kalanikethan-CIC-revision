package com.lovinsharma.kalanikethancic.data.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "family_table")
data class Family(
    @ColumnInfo(name = "family_ID") @PrimaryKey(autoGenerate = true) val familyID: Int=0,
    @ColumnInfo(name = "family_name") val familyname: String,
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
    @ColumnInfo(name = "student_name") val studentName: String,
    @ColumnInfo(name = "student_number") val studentNumber: String,
    @ColumnInfo(name = "student_dob") val birthdate: String,
    @ColumnInfo(name = "can_walk_alone") val canWalkAlone: Boolean = false,
    @ColumnInfo(name = "signed_in") val signedIn: Boolean = false,
    @ColumnInfo(name = "family_ID") val familyIDfk: Int,
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
    @ColumnInfo(name = "parent_name") val parentName: String,
    @ColumnInfo(name = "parent_number") val parentNumber: Int,
    @ColumnInfo(name = "parent_email") val parentEmail: String,
    @ColumnInfo(name = "payment_date") val paymentDate: String,
    @ColumnInfo(name = "family_ID") val familyIDfk: Int,
)

