package com.lovinsharma.kalanikethancic.Screens

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import com.lovinsharma.kalanikethancic.Viewmodel.MyViewModel
import com.lovinsharma.kalanikethancic.data.room.models.Event
import com.lovinsharma.kalanikethancic.ui.theme.background
import com.lovinsharma.kalanikethancic.ui.theme.selectedLight
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WhoInScreen(viewModel: MyViewModel) {
    // Collect paged students
    val pagedStudents = viewModel.signedInStudents.observeAsState(emptyList())
    // Get the current time
    val currentTime = LocalTime.now()
    // Format the time as "HH:mm"
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val newSignOutTime = currentTime.format(formatter)



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 0.dp, vertical = 0.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(0.dp))
                    .background(color = selectedLight)
                    .padding(horizontal = 30.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Sign In",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .wrapContentHeight(align = Alignment.CenterVertically),
                    textAlign = TextAlign.Right
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Replace LazyColumn with a regular Column
            for (student in pagedStudents.value) {
                StudentBox(
                    studentName = student.studentName,
                    dateOfBirth = student.birthdate,
                    contactInfo = student.studentNumber,
                    canLeaveAlone = student.canWalkAlone,
                    onSignIn = {
                        viewModel.updateStudentStatus(
                            id = student.studentID,
                            status = false,
                        )


                        viewModel.updateSignOutTime(student.studentID, newSignOutTime)

                    },
                    buttonstring = "Sign out",
                    showabsent = false,
                    formattedday = "",
                    student = student,
                    viewModel = viewModel
                )
            }
        }
    }
}
