package com.lovinsharma.kalanikethancic.Screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.lovinsharma.kalanikethancic.Viewmodel.MyViewModel
import com.lovinsharma.kalanikethancic.data.room.models.Event
import com.lovinsharma.kalanikethancic.data.room.models.Students
import com.lovinsharma.kalanikethancic.ui.theme.PrimaryLightColor
import com.lovinsharma.kalanikethancic.ui.theme.background
import com.lovinsharma.kalanikethancic.ui.theme.selectedLight
import java.time.LocalTime
import java.time.format.DateTimeFormatter


import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// Function to get the current day in the format "Saturday 28th September"
fun getFormattedDay(): String {
    // Get the current date
    val currentDate = Date()

    // Define the desired date format
    val dateFormat = SimpleDateFormat("EEEE d'th' MMMM", Locale.getDefault())

    // Format the current date
    return dateFormat.format(currentDate)
}



@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignInScreen(viewModel: MyViewModel) {
    // State for search input
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var searchQuery by remember { mutableStateOf("") }

    // Collect the filtered students from the ViewModel
    val pagedUnsignedStudents: LazyPagingItems<Students> = viewModel.pagedUnsignedStudents.collectAsLazyPagingItems()

    // Get the current time
    val currentTime = LocalTime.now()
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val signInTime = currentTime.format(formatter)
    val formattedDay = getFormattedDay()

    // Single LazyColumn to handle the entire UI
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = background)
    ) {
        // Header with Sign In text
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(0.dp))
                    .background(color = selectedLight)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left-aligned "Sign In" text
                    Text(
                        text = "Sign In",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Left
                    )
                    // Optional: Search field if you want to add it back later
                    /*
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { newValue ->
                            searchQuery = newValue
                        },
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Start
                        ),
                        modifier = Modifier // Ensures text field takes up as much width as possible
                            .focusRequester(focusRequester),
                        singleLine = true,
                        cursorBrush = SolidColor(Color.White),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text, // Capitalize first letter of each word
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                keyboardController?.hide()
                            }
                        ),
                    )
                    */
                }
            }
        }

        // Spacer between header and list
        item {
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Display the filtered list of students
        items(pagedUnsignedStudents.itemCount) { index ->
            val student = pagedUnsignedStudents[index]
            student?.let {
                StudentBox(
                    studentName = student.studentName,
                    dateOfBirth = student.birthdate,
                    contactInfo = student.studentNumber,
                    canLeaveAlone = student.canWalkAlone,
                    onSignIn = {
                        viewModel.updateStudentStatus(
                            id = student.studentID,
                            status = true
                        )
                        viewModel.upsertEvent(
                            Event(
                                signIn = signInTime,
                                day = formattedDay,
                                signOut = "",
                                studentIDfk = student.studentID,
                                isOpen = true,
                                studentName = student.studentName
                            )
                        )
                    },
                    buttonstring = "Sign In",
                    showabsent = true,
                    viewModel = viewModel,
                    student = student,
                    formattedday = formattedDay
                )
            }
        }
    }
}





//                    BasicTextField(
//                        value = searchQuery,
//                        onValueChange = { newValue ->
//                            searchQuery = newValue
//                        },
//                        textStyle = TextStyle(
//                            color = Color.White,
//                            fontSize = 20.sp,
//                            fontWeight = FontWeight.Normal,
//                            textAlign = TextAlign.Start
//                        ),
//                        modifier = Modifier // Ensures text field takes up as much width as possible
//                            .focusRequester(focusRequester),
//                        singleLine = true,
//                        cursorBrush = SolidColor(Color.White),
//                        keyboardOptions = KeyboardOptions.Default.copy(
//                            imeAction = ImeAction.Done,
//                            keyboardType = KeyboardType.Text, // Capitalize first letter of each word
//                        ),
//                        keyboardActions = KeyboardActions(
//                            onDone = {
//                                focusManager.clearFocus()
//                                keyboardController?.hide()
//                            }
//                        ),
//
//                    )








@Composable
fun onAbsent(
    viewModel: MyViewModel, // Pass any required parameters here
    student: Students, // Example parameter, like the student object
    formattedDay: String, // Example for day formatting
    showDialog: MutableState<Boolean>,
) {
    var reasonForAbsence by remember { mutableStateOf("") }
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Reason for Absence") },
            text = {
                Column {
                    Text("Please enter the reason for absence:")
                    TextField(
                        value = reasonForAbsence,
                        onValueChange = { reasonForAbsence = it },
                        placeholder = { Text("Reason") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (reasonForAbsence.isNotEmpty()) {
                        // Insert the event with the reason for absence
                        viewModel.upsertEvent(
                            Event(
                                signIn = "",
                                day = formattedDay,
                                signOut = "",
                                studentIDfk = student.studentID,
                                isOpen = false,
                                studentName = student.studentName,
                                isAbsent = true,
                                reasonForAbsence = reasonForAbsence
                            )
                        )

                        // Hide dialog and clear input
                        showDialog.value = false
                    }
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = {
                    // Clear input and close dialog
                    reasonForAbsence = ""
                    showDialog.value = false
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}








@Composable
fun StudentBox(
    student: Students,
    studentName: String,
    dateOfBirth: String,
    contactInfo: String,
    canLeaveAlone: Boolean,
    buttonstring: String,
    onSignIn: () -> Unit,
    showabsent: Boolean,
    viewModel: MyViewModel,
    formattedday: String
) {
    var showdialogue = remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .background(Color.White)
            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Name Column
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Name",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
                Text(
                    text = studentName,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            // DOB Column
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "DOB",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
                Text(
                    text = dateOfBirth,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            // Contact Info Column
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Student Number",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
                Text(
                    text = contactInfo,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            // Can Leave Alone Checkbox
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Can leave alone",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
                Checkbox(
                    checked = canLeaveAlone,
                    onCheckedChange = null, // Make it read-only
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            // Sign In Button
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Button(
                    onClick = onSignIn,
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1b69b2)),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = buttonstring,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }
            }

            // Absent Button (if enabled)
            if (showabsent) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Button(
                        onClick = { showdialogue.value = true },
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1b69b2)),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = "Absent",
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    }

                }
            }

            if (showdialogue.value) {
                onAbsent(
                    viewModel = viewModel,
                    student = student,
                    formattedDay = formattedday,
                    showDialog = showdialogue,
                )
            }
        }
    }
}


