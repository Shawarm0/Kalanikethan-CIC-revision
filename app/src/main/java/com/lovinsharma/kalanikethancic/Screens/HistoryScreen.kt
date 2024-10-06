package com.lovinsharma.kalanikethancic.Screens

import android.content.Context
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lovinsharma.kalanikethancic.Viewmodel.MyViewModel
import com.lovinsharma.kalanikethancic.data.room.models.Event
import com.lovinsharma.kalanikethancic.ui.theme.background
import com.lovinsharma.kalanikethancic.ui.theme.selectedLight

@Composable
fun HistoryScreen(viewModel: MyViewModel) {
    // Observe the history from the ViewModel
    val history by viewModel.history.observeAsState(emptyList())

    // Group events by day and sort by isOpen
    val groupedHistory = history
        .groupBy { it.day } // Group by day
        .mapValues { entry -> // Sort events by isOpen within each group
            entry.value.sortedByDescending { it.isOpen }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = background)
            .padding(horizontal = 0.dp, vertical = 0.dp)
    ) {
        // Box at the top for "History"
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
                text = "History",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(horizontal = 0.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Display each group of events in a DayHistoryBox
        groupedHistory.forEach { (day, events) ->
            DayHistoryBox(day = day, events = events)
            Spacer(modifier = Modifier.height(16.dp)) // Space between day sections
        }
    }
}

@Composable
fun DayHistoryBox(day: String, events: List<Event>) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight() // Adjust height based on content
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(Color.White)
            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            // Display the day as the header
            Text(
                text = day,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // For each event in the list, display the details
            events.forEach { event ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Name Column (Assuming name can be retrieved via a relationship, adjust accordingly)
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Name:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        // Placeholder for the name (modify if you have student name available)
                        Text(
                            text = event.studentName, // Adjust based on available data
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    // Sign in Column
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Sign in:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        Text(
                            text = event.signIn ?: "N/A",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    // Sign out Column
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Sign out:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        Text(
                            text = if (event.isOpen) "" else event.signOut ?: "N/A",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}