package com.lovinsharma.kalanikethancic.Screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lovinsharma.kalanikethancic.R
import com.lovinsharma.kalanikethancic.Viewmodel.MyViewModel
import com.lovinsharma.kalanikethancic.data.room.models.Students
import com.lovinsharma.kalanikethancic.ui.theme.background
import com.lovinsharma.kalanikethancic.ui.theme.selectedLight
import java.util.Calendar
import java.util.Locale
import java.text.SimpleDateFormat
import java.util.Date
import androidx.compose.material3.DatePicker
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.vector.ImageVector
import com.lovinsharma.kalanikethancic.data.room.models.Family
import com.lovinsharma.kalanikethancic.data.room.models.Parents
import com.lovinsharma.kalanikethancic.ui.theme.PrimaryLightColor
import kotlinx.coroutines.*

val studentsList = mutableListOf<Students>()
val parentsList = mutableListOf<Parents>()
val family = mutableStateOf<Family>(Family(
    familyID = 0,
    familyname = "",
    paymentID = ""
))

fun addNewStudent(viewModel: MyViewModel) {
    // Check if the list is not empty
    if (studentsList.isNotEmpty()) {
        // Get the last item in the list
        val lastStudent = studentsList.last()

        // Run viewModel.addStudent using the last student
        viewModel.addStudent(lastStudent)
    } else {
        println("The students list is empty.")
    }
}



fun saveall(viewModel: MyViewModel) {
    viewModel.addFamily(Family(
        familyname = family.value.familyname,
        paymentID = family.value.paymentID
    ))
    // Reset the family MutableState to a new default Family object
    family.value = Family(
        familyID = 0,
        familyname = "",
        paymentID = ""
    )

    for (student in studentsList) {
        viewModel.addStudent(student)
    }
    studentsList.clear()

    for (parent in parentsList) {
        viewModel.addParent(parent)
    }
    parentsList.clear()
}




@Composable
fun AddScreen(viewModel: MyViewModel) {

    // Variables required for data management stuff
    var familyName by remember { mutableStateOf("") }
    var familyhasbeenadded = remember { mutableStateOf(false) }
    var currentFamilyId: Int by remember { mutableStateOf(0) }


    // This is for text display
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val lastFamilyID by viewModel.lastFamily.observeAsState()
    var familyIdInt = 0
    var paymentID by remember { mutableStateOf("") }

    lastFamilyID?.let { familyId ->
        familyIdInt = familyId.toInt() // Convert Long to Int
    }

    // This is just for navigation and changing screens
    val navController = rememberNavController()

    var showAddButton by remember { mutableStateOf(false) }
    var addstate by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = background)
            .padding(horizontal = 0.dp, vertical = 0.dp)
    ) {
        // Box at the top for "Add Student"
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(0.dp))
                .background(color = selectedLight)
                .padding(horizontal = 30.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart), // Align Row to center vertically within the Box
                verticalAlignment = Alignment.CenterVertically, // Align content vertically in the Row
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Placeholder Text and Text Field
                Box(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    if (familyName.isEmpty()) {
                        Text(
                            text = "Enter Family Name",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Start
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp) // Adjust padding to align vertically
                        )
                    }


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            value = familyName,
                            onValueChange = { input ->
                                familyName = input.split(" ")
                                    .joinToString(" ") { it.capitalize(Locale.ROOT) }
                                showAddButton =
                                    familyName.isNotBlank() // Show button if there's input
                            },
                            textStyle = TextStyle(
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Start
                            ),
                            modifier = Modifier
                                .weight(1f) // Ensures text field takes up as much width as possible
                                .focusRequester(focusRequester),
                            singleLine = true,
                            cursorBrush = SolidColor(Color.White),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Text,
                                capitalization = KeyboardCapitalization.Words // Capitalize first letter of each word
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                    keyboardController?.hide()
                                }
                            )
                        )

                        if (showAddButton) {


                            val buttonColors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent, // Transparent by default
                                contentColor = Color.White // White text color
                            )

                            val selectedButtonColors = ButtonDefaults.buttonColors(
                                containerColor = selectedLight, // Use selected color for active state
                                contentColor = Color.White // White text color
                            )

                            if (addstate) {
                                IconButtonWithText(
                                    onClick = {
                                        // Run your code to add the family here
                                        family.value =
                                            Family(
                                                familyID = familyIdInt+1,
                                                familyname = familyName,
                                                paymentID = ""
                                            )
                                        // Hide the button after family is added
                                        showAddButton = false
                                        addstate = true
                                        currentFamilyId = familyIdInt
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit Family",
                                            tint = Color.White
                                        )
                                    },
                                    text = "Edit Family",
                                    buttonColors = ButtonDefaults.buttonColors(
                                        containerColor = PrimaryLightColor,
                                        contentColor = Color.White
                                    )
                                )
                            } else {
                                IconButtonWithText(
                                    onClick = {
                                        // Run your code to add the family here
                                        family.value =
                                            Family(
                                                familyID = familyIdInt+1,
                                                familyname = familyName,
                                                paymentID = ""
                                            )
                                        // Hide the button after family is added
                                        showAddButton = false
                                        addstate = true
                                        currentFamilyId = familyIdInt + 1
                                        familyhasbeenadded.value = true
                                        println(currentFamilyId)
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Default.Face,
                                            contentDescription = "Add Family",
                                            tint = Color.White
                                        )
                                    },
                                    text = "Add Family",
                                    buttonColors = ButtonDefaults.buttonColors(
                                        containerColor = PrimaryLightColor,
                                        contentColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }







                    // BUTTONS AT THE TOP
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                   AddScreenButtons(iconResId = R.drawable.student,
                       text = "Students",
                       isSelected = true,
                       onClick = { navController.navigate("Students") })

                    Spacer(modifier = Modifier.width(8.dp)) // Space between buttons
                    AddScreenButtons(iconResId = R.drawable.parents,
                        text = "Parents",
                        isSelected = true,
                        onClick = { navController.navigate("Parents") })

                    Spacer(modifier = Modifier.width(8.dp)) // Space between buttons
                    AddScreenButtons(iconResId = R.drawable.payid,
                        text = "Payment ID",
                        isSelected = true,
                        onClick = { navController.navigate("Payment ID") })

                    }
                }
            }


        // Method of managing screens
        NavHost(navController = navController, startDestination = "Students" ) {
            composable("Students") { StudentsScreen(navController, currentFamilyId, familyhasbeenadded, viewModel) }
            composable("Parents") { ParentsScreen(navController, currentFamilyId, familyhasbeenadded, viewModel) }
            composable("Payment ID") { PaymentIDScreen(navController, family, viewModel) }
        }

    }

}



@Composable
fun DialogExamples(
    openAlertDialog: MutableState<Boolean>,
    onConfirm: () -> Unit
) {
    if (openAlertDialog.value) {
        AlertDialog(
            onDismissRequest = { openAlertDialog.value = false },
            confirmButton = {
                TextButton(onClick = {
                    onConfirm()
                    openAlertDialog.value = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    openAlertDialog.value = false
                }) {
                    Text("Cancel")
                }
            },
            title = { Text(text = "Warning") },
            text = { Text(text = "Please add family before adding students!") },
            icon = { Icon(Icons.Default.Info, contentDescription = null) }
        )
    }
}

@Composable
fun StudentsScreen(
    navController: NavController,
    currentFamilyID: Int?,
    familyHasBeenAdded: MutableState<Boolean>,
    viewModel: MyViewModel
) {
    // State to control dialog visibility
    val openAlertDialog = remember { mutableStateOf(false) }

    // State to trigger recomposition when the list changes
    val studentsStateList = remember { studentsList.toMutableStateList() }

    // Creates a scrollable box
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Display each student with their editable content
            for (student in studentsStateList) {
                StudentContent(
                    student = student,
                    onRemove = {
                        studentsStateList.remove(student) // Remove student from the list
                        studentsList.remove(student) // Also remove it from the original top-level list
                    }
                )
            }
        }

        // Floating Action Button positioned at the bottom right
        ExtendedFloatingActionButton(
            onClick = {
                if (familyHasBeenAdded.value) {
                    // Create a new empty student and add it to the lists
                    val newStudent = Students(
                        studentName = "",
                        studentNumber = "",
                        birthdate = "",
                        signedIn = false,
                        canWalkAlone = false,
                        familyIDfk = currentFamilyID ?: -1,
                    )
                    studentsStateList.add(newStudent) // Add to the composable's list
                    studentsList.add(newStudent) // Add to the top-level list
                    println(studentsList)
                } else {
                    openAlertDialog.value = true
                }
            },
            icon = { Icon(Icons.Filled.Add, contentDescription = "Add Student") },
            text = { Text(text = "Add Student") },
            containerColor = selectedLight,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(horizontal = 16.dp, vertical = 66.dp)
        )

        // Show dialog if necessary
        DialogExamples(openAlertDialog) {
            println("Confirmation registered")
        }
    }
}







@Composable
fun ParentsScreen(navController: NavController,
                  currentFamilyID: Int?,
                  familyHasBeenAdded: MutableState<Boolean>,
                  viewModel: MyViewModel) {
    // State to control dialog visibility
    val openAlertDialog = remember { mutableStateOf(false) }


    // State to trigger recomposition when the list changes
    val parentsStateList = remember { parentsList.toMutableStateList() }

    // Creates a scrollable box
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Display each student with their editable content
            for (parent in parentsStateList) {
                ParentContent(
                    parent = parent,
                    onRemove = {
                        parentsStateList.remove(parent) // Remove student from the list
                        parentsList.remove(parent) // Also remove it from the original top-level list
                    }
                )
            }
        }

        // Floating Action Button positioned at the bottom right
        ExtendedFloatingActionButton(
            onClick = {
                if (familyHasBeenAdded.value) {
                    // Create a new empty student and add it to the lists
                    val newParent = Parents(
                        parentName = "",
                        parentNumber = "",
                        parentEmail = "",
                        paymentDate = "",
                        familyIDfk = currentFamilyID ?: -1,
                    )
                    parentsStateList.add(newParent) // Add to the composable's list
                    parentsList.add(newParent) // Add to the top-level list
                    println(parentsList)
                } else {
                    openAlertDialog.value = true
                }
            },
            icon = { Icon(Icons.Filled.Add, contentDescription = "Add Parent") },
            text = { Text(text = "Add Parent") },
            containerColor = selectedLight,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(horizontal = 16.dp, vertical = 66.dp)
        )

        // Show dialog if necessary
        DialogExamples(openAlertDialog) {
            println("Confirmation registered")
        }
    }


}






@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentIDScreen(navController: NavController, family: MutableState<Family>, viewModel: MyViewModel) {

    var paymentID by remember { mutableStateOf(family.value.paymentID) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var hascontent = (paymentID != "")

    val initialpaymentID = family.value.paymentID

    val isValid = (initialpaymentID != paymentID)



    Box(
        modifier = Modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(15.dp)) // Apply rounded corners
            .background(color = Color.White) // Background color
        // Padding inside the box
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(modifier = Modifier.padding(horizontal = 16.dp)) { // Added padding for better spacing
                OutlinedTextField(
                    value = paymentID,
                    onValueChange = { input -> paymentID = input },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 20.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.Transparent
                    ),
                    placeholder = { Text("Enter paymentID") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = "Payment Icon"
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Words
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                    )
                )

            }

            if (isValid && hascontent) {
                Button(
                    onClick = {

                        println(family)
                        println(studentsList)
                        println(parentsList)


                        saveall(viewModel) }, // Calls the onRemove lambda to terminate the composable
                    colors = ButtonDefaults.buttonColors(containerColor = selectedLight),
                    shape = RoundedCornerShape(15.dp), // Adjust roundness of the edges (reduce it)
                    modifier = Modifier.padding(0.dp) // Optional: add some padding around the button
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle, // Replace with any icon you want
                        contentDescription = "Save Icon",
                        tint = Color.White, // Set icon color
                        modifier = Modifier.size(20.dp) // Adjust icon size if necessary
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Adds space between icon and text
                    Text(text = "Save", color = Color.White)
                }
            }


        }

    }
}



@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}







@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentContent(
    parent: Parents,
    onRemove: () -> Unit
) {
    var parentName by remember { mutableStateOf(parent.parentName) }
    var parentNumber by remember { mutableStateOf(parent.parentNumber) }
    var parentEmail by remember { mutableStateOf(parent.parentEmail) }
    var paymentDate by remember { mutableStateOf(parent.paymentDate) }
    var isValid by remember { mutableStateOf(false) }
    var updatebuttonstate by remember { mutableStateOf(false) }

    // Store the initial values when the composable is first loaded
    val initialParentName = remember { parent.parentName }
    val initialParentNumber = remember { parent.parentNumber }
    val initialParentEmail = remember { parent.parentEmail }
    val initialPaymentDate = remember { parent.paymentDate }

    // This effect triggers only when a field value changes
    LaunchedEffect(parentName, parentNumber, parentEmail, paymentDate) {
        println("Validating fields.")
        println("Name: $parentName, Number: $parentNumber, Email: $parentEmail, Payment Date: $paymentDate")

        // Call your validation logic
        isValid = validateParentFields(parentName, parentNumber, parentEmail, paymentDate)

        // Only show the update button if the fields are valid and have changed
        updatebuttonstate = isValid && (
                parentName != initialParentName ||
                        parentNumber != initialParentNumber ||
                        parentEmail != initialParentEmail ||
                        paymentDate != initialPaymentDate
                )

        println("Is Valid: $isValid, Update Button State: $updatebuttonstate")
    }

    // This is for text display
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current


    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()



    Box(
        modifier = Modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(15.dp)) // Apply rounded corners
            .background(color = if (isValid) Color.Green.copy(alpha = 0.05f) else Color.White) // Background color
        // Padding inside the box
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            // Title and Remove button in a Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween, // Pushes content to the left and right
                verticalAlignment = Alignment.CenterVertically
            ) {

                Button(
                    onClick = { onRemove() }, // Calls the onRemove lambda to terminate the composable
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    shape = RoundedCornerShape(15.dp), // Adjust roundness of the edges (reduce it)
                    modifier = Modifier.padding(0.dp) // Optional: add some padding around the button
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete, // Replace with any icon you want
                        contentDescription = "Remove Icon",
                        tint = Color.White, // Set icon color
                        modifier = Modifier.size(20.dp) // Adjust icon size if necessary
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Adds space between icon and text
                    Text(text = "Remove", color = Color.White)
                }

                // Shared button and box style
                val sharedModifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .padding(start = 150.dp) // Align the button/box at the end of the row
                    .fillMaxWidth()


                if (isValid && updatebuttonstate) {
                    // Button when the form is valid
                    Button(
                        onClick = {
                            parent.parentName = parentName
                            parent.parentNumber = parentNumber
                            parent.parentEmail = parentEmail
                            parent.paymentDate = paymentDate
                            // Optionally call a function to notify the view model
                            // that the student has been updated
                            updatebuttonstate = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Green.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(15.dp),
                        modifier = sharedModifier
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Check Icon",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Update", color = Color.White)
                    }
                }

            }

            Row(modifier = Modifier.padding(horizontal = 16.dp)) { // Added padding for better spacing
                OutlinedTextField(
                    value = parentName,
                    onValueChange = { input -> parentName = input },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 20.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = if (isValid) Color.Green.copy(alpha = 0.1f) else Color.Transparent
                    ),
                    placeholder = { Text("Enter parent name") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Person Icon"
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Words
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                    )
                )

                OutlinedTextField(
                    value = parentNumber,
                    onValueChange = { input -> parentNumber = input },
                    modifier = Modifier
                        .weight(1f),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = if (isValid) Color.Green.copy(alpha = 0.1f) else Color.Transparent
                    ),
                    placeholder = { Text("Enter parent number") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Phone Icon") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.NumberPassword,
                        capitalization = KeyboardCapitalization.Words
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                    ),
                )

            }

            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) { // Added padding for better spacing
                OutlinedTextField(
                    value = parentEmail,
                    onValueChange = { input -> parentEmail = input },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 20.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = if (isValid) Color.Green.copy(alpha = 0.1f) else Color.Transparent
                    ),
                    placeholder = { Text("Enter parent email") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = "Email Icon"
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                    )
                )

                OutlinedTextField(
                    value = paymentDate,
                    onValueChange = { newDate -> paymentDate = newDate }, // Allow user input
                    modifier = Modifier
                        .width(300.dp)
                        .padding(end = 20.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = if (isValid) Color.Green.copy(alpha = 0.1f) else Color.Transparent
                    ),
                    placeholder = { Text("Enter payment date") },
                    leadingIcon = {
                        IconButton(onClick = { showDatePicker = !showDatePicker }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Select date"
                            )
                        }
                    },
                    readOnly = true, // Make read-only to prevent manual entry
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Words
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                    )
                )







            }


            // Set dateOfBirth from the date picker
            if (showDatePicker) {
                AlertDialog(
                    onDismissRequest = { showDatePicker = false },
                    title = { Text("Select Date") },
                    text = {
                        Column {
                            DatePicker(
                                state = datePickerState,
                                showModeToggle = false
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            val selectedMillis = datePickerState.selectedDateMillis
                            if (selectedMillis != null) {
                                val calendar = Calendar.getInstance().apply {
                                    timeInMillis = selectedMillis
                                }
                                // Format day and month to always have two digits (dd/mm/yyyy)
                                val day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))
                                val month = String.format("%02d", calendar.get(Calendar.MONTH) + 1) // Months are zero-based
                                val year = calendar.get(Calendar.YEAR)

                                // Update dateOfBirth with the formatted date string
                                paymentDate = "$day/$month/$year"
                            }
                            showDatePicker = false
                        }) {
                            Text("OK")
                        }
                    },
                )
            }




        }


    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentContent(
    student: Students, // Pass the student object
    onRemove: () -> Unit // Keep the remove functionality
) {
    var studentName by remember { mutableStateOf(student.studentName) }
    var studentNumber by remember { mutableStateOf(student.studentNumber) }
    var canWalkAlone by remember { mutableStateOf(student.canWalkAlone) }
    var dateOfBirth by remember { mutableStateOf(student.birthdate) }
    var isValid by remember { mutableStateOf(false) }
    var updatebuttonstate by remember { mutableStateOf(false) }

    // Initial states for comparison
    val initialStudentName = student.studentName
    val initialStudentNumber = student.studentNumber
    val initialDateOfBirth = student.birthdate

    // This launches a coroutine and validates the fields asynchronously
    LaunchedEffect(studentName, studentNumber, dateOfBirth) {
        println("Validating fields:")
        println("Name: $studentName, Number: $studentNumber, DOB: $dateOfBirth")

        // Validate fields
        isValid = validateFields(studentName, studentNumber, dateOfBirth)

        // Only show the update button if something has changed
        updatebuttonstate = isValid &&
                (studentName != initialStudentName || studentNumber != initialStudentNumber || dateOfBirth != initialDateOfBirth)

        println("Is Valid: $isValid")
        println("Update Button State: $updatebuttonstate")
    }




    // This is for text display
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current


    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()



    Box(
        modifier = Modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(15.dp)) // Apply rounded corners
            .background(color = if (isValid) Color.Green.copy(alpha = 0.05f) else Color.White) // Background color
             // Padding inside the box
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            // Title and Remove button in a Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween, // Pushes content to the left and right
                verticalAlignment = Alignment.CenterVertically
            ) {

                Button(
                    onClick = { onRemove() }, // Calls the onRemove lambda to terminate the composable
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    shape = RoundedCornerShape(15.dp), // Adjust roundness of the edges (reduce it)
                    modifier = Modifier.padding(0.dp) // Optional: add some padding around the button
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete, // Replace with any icon you want
                        contentDescription = "Remove Icon",
                        tint = Color.White, // Set icon color
                        modifier = Modifier.size(20.dp) // Adjust icon size if necessary
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Adds space between icon and text
                    Text(text = "Remove", color = Color.White)
                }

            }

            Row(modifier = Modifier.padding(horizontal = 16.dp)) { // Added padding for better spacing
                OutlinedTextField(
                    value = studentName,
                    onValueChange = { input -> studentName = input },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 20.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = if (isValid) Color.Green.copy(alpha = 0.1f) else Color.Transparent
                    ),
                    placeholder = { Text("Enter student name") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Person Icon"
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Words
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                    )
                )

                OutlinedTextField(
                    value = studentNumber,
                    onValueChange = { input -> studentNumber = input },
                    modifier = Modifier
                        .weight(1f),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = if (isValid) Color.Green.copy(alpha = 0.1f) else Color.Transparent
                    ),
                    placeholder = { Text("Enter student number") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Phone Icon") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.NumberPassword,
                        capitalization = KeyboardCapitalization.Words
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                    ),
                )

            }

            Row(modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,) { // Added padding for better spacing

                // Use dateOfBirth in the OutlinedTextField
                OutlinedTextField(
                    value = dateOfBirth,
                    onValueChange = { newDate -> dateOfBirth = newDate }, // Allow user input
                    modifier = Modifier
                        .width(300.dp)
                        .padding(end = 20.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = if (isValid) Color.Green.copy(alpha = 0.1f) else Color.Transparent
                    ),
                    placeholder = { Text("Enter date of birth") },
                    leadingIcon = {
                        IconButton(onClick = { showDatePicker = !showDatePicker }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Select date"
                            )
                        }
                    },
                    readOnly = true, // Make read-only to prevent manual entry
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Words
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                    )
                )

                Switch(
                    checked = canWalkAlone,
                    onCheckedChange = { isChecked ->
                        canWalkAlone = isChecked // Update state when toggled
                    }, thumbContent = if (canWalkAlone) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        }
                    } else {
                        null
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                        uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                        uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                    )

                )
                Text(text = if (canWalkAlone) "Can walk home alone!" else "Cannot walk home alone!",
                    modifier = Modifier.padding(start= 8.dp))




                // Shared button and box style
                val sharedModifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .padding(start = 150.dp) // Align the button/box at the end of the row
                    .fillMaxWidth()
                    .height(56.dp)

                if (isValid && updatebuttonstate) {
                    // Button when the form is valid
                    Button(
                        onClick = {
                            student.studentName = studentName
                            student.studentNumber = studentNumber
                            student.canWalkAlone = canWalkAlone
                            student.birthdate = dateOfBirth
                            // Optionally call a function to notify the view model
                            // that the student has been updated
                            updatebuttonstate = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Green.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(15.dp),
                        modifier = sharedModifier
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Check Icon",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Update", color = Color.White)
                    }
                }



            }

            // Set dateOfBirth from the date picker
            if (showDatePicker) {
                AlertDialog(
                    onDismissRequest = { showDatePicker = false },
                    title = { Text("Select Date") },
                    text = {
                        Column {
                            DatePicker(
                                state = datePickerState,
                                showModeToggle = false
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            val selectedMillis = datePickerState.selectedDateMillis
                            if (selectedMillis != null) {
                                val calendar = Calendar.getInstance().apply {
                                    timeInMillis = selectedMillis
                                }
                                // Format day and month to always have two digits (dd/mm/yyyy)
                                val day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))
                                val month = String.format("%02d", calendar.get(Calendar.MONTH) + 1) // Months are zero-based
                                val year = calendar.get(Calendar.YEAR)

                                // Update dateOfBirth with the formatted date string
                                dateOfBirth = "$day/$month/$year"
                            }
                            showDatePicker = false
                        }) {
                            Text("OK")
                        }
                    },
                )
            }



                // Validation status
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = if (isValid) "All fields are valid!" else "Please correct the input fields.",
                color = if (isValid) Color.Green else Color.Red
            )
        }
    }
}


suspend fun validateFields(
    name: String,
    number: String,
    dob: String
): Boolean {
    // Validate name (allow letters, spaces, hyphens, and apostrophes)
    val nameRegex = "^[A-Za-z\\s'-]+$".toRegex()
    if (!name.matches(nameRegex)) return false

    // Validate student number (must start with 0 and be exactly 11 digits)
    val numberRegex = "^0\\d{10}$".toRegex()
    if (!number.matches(numberRegex)) return false

    // Validate date of birth (format dd/mm/yyyy)
    val dobRegex = "^\\d{2}/\\d{2}/\\d{4}$".toRegex()
    if (!dob.matches(dobRegex)) return false

    // Additional date validation can be added here if necessary
    return true
}




suspend fun validateParentFields(
    name: String,
    number: String,
    email: String,
    paymentdate: String
): Boolean {
    // Validate name (allow letters, spaces, hyphens, and apostrophes)
    val nameRegex = "^[A-Za-z\\s'-]+$".toRegex()
    if (!name.matches(nameRegex)) return false

    // Validate student number (must start with 0 and be exactly 11 digits)
    val numberRegex = "^0\\d{10}$".toRegex()
    if (!number.matches(numberRegex)) return false

    val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
    if (!email.matches(emailRegex)) return false

    // Validate date of birth (format dd/mm/yyyy)
    val dobRegex = "^\\d{2}/\\d{2}/\\d{4}$".toRegex()
    if (!paymentdate.matches(dobRegex)) return false

    // Additional date validation can be added here if necessary
    return true
}

@Composable
fun IconButtonWithText(
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    buttonColors: ButtonColors = ButtonDefaults.buttonColors()
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .padding(start = 8.dp)
            .width(180.dp), // Adjust the width based on content
        colors = buttonColors,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Leading Icon
            icon()

            Spacer(modifier = Modifier.width(8.dp)) // Space between icon and text

            // Text
            Text(
                text = text,
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}











@Composable
fun AddScreenButtons(iconResId: Int, text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.width(180.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) selectedLight else Color.Transparent
        ),
        shape = MaterialTheme.shapes.medium // Use MaterialTheme's default button shape
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Use CustomIcon composable to display the custom icon
            CustomIcon(iconResId = iconResId, contentDescription = text)

            Spacer(modifier = Modifier.width(8.dp)) // Adjust spacing as needed

            Text(
                text = text,
                color = Color.White, // Text color
                fontSize = 16.sp,
                modifier = Modifier.padding(end = 8.dp) // Add padding between icon and text
            )
        }
    }
}

@Composable
fun CustomIcon(iconResId: Int, contentDescription: String) {
    // Load the custom icon using painterResource
    val painter = painterResource(id = iconResId)

    // Display the icon using Icon composable
    Icon(
        painter = painter,
        contentDescription = contentDescription,
        modifier = Modifier
            .padding(0.dp)
            .size(25.dp) // Adjust padding as needed
    )
}


fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}