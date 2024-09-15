package com.lovinsharma.kalanikethancic

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider


import com.lovinsharma.kalanikethancic.ui.theme.KalanikethanCICTheme
import com.lovinsharma.kalanikethancic.ui.theme.PrimaryLightColor
import com.lovinsharma.kalanikethancic.ui.theme.selectedLight


//Import screen
import com.lovinsharma.kalanikethancic.Screens.SignInScreen
import com.lovinsharma.kalanikethancic.Screens.AddScreen
import com.lovinsharma.kalanikethancic.Screens.WhoInScreen
import com.lovinsharma.kalanikethancic.Screens.HistoryScreen
import com.lovinsharma.kalanikethancic.Screens.PaymentsScreen
import com.lovinsharma.kalanikethancic.Viewmodel.MyViewModel
import com.lovinsharma.kalanikethancic.Viewmodel.MyViewModelFactory
import com.lovinsharma.kalanikethancic.data.repository.Repository
import com.lovinsharma.kalanikethancic.data.room.AppDatabase



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KalanikethanCICTheme {
                var selectedScreen by remember { mutableStateOf("signIn") } //Defines home screen and says that the selected screen can change

                val database = AppDatabase.getDatabase(applicationContext)

                val familyDao = database.familyDao()
                val studentDao = database.studentDao()
                val parentsDao = database.parentsDao()
                val repository = Repository(familyDao, studentDao, parentsDao)
                val viewModelFactory = MyViewModelFactory(repository)
                val myViewModel = ViewModelProvider(this, viewModelFactory)[MyViewModel::class.java]


                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding -> //Layout feature it fills the maximum size

                    Row(Modifier.fillMaxSize()) { //Now we've got a row which basically splits the screen into 2
                        Appbar(
                            modifier = Modifier
                                .padding(innerPadding)
                                .width(200.dp)
                                .fillMaxHeight(),
                            onScreenSelected = { screen ->
                                selectedScreen = screen
                            }
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 24.dp)
                        ) {



                            when (selectedScreen) {
                                "signIn" -> SignInScreen()
                                "add" -> AddScreen(viewModel = myViewModel)
                                "whoin" -> WhoInScreen()
                                "history" -> HistoryScreen()
                                "payments" -> PaymentsScreen()
                            }
                        }
                    }
                }
            }
        }
    }




// This is all the appbar
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

@Composable
fun Appbar(
    modifier: Modifier = Modifier,
    onScreenSelected: (String) -> Unit // Add this parameter
) {
    var selectedScreen by remember { mutableStateOf("screen1") } // Track selected screen

    Surface(
        modifier = modifier
            .height(500.dp)
            .width(200.dp),
        color = PrimaryLightColor, // AppBar background color
        shape = RoundedCornerShape(0.dp),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo), // Replace with your logo
                contentDescription = "App Logo",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Inside Appbar composable
            AppBarButton(
                iconResId = R.drawable.login,
                text = " Sign In",
                isSelected = selectedScreen == "screen1",
                onClick = {
                    selectedScreen = "screen1"
                    onScreenSelected("signIn")
                }
            )


            // Button 2
            AppBarButton(
                iconResId = R.drawable.add,
                text = " Add",
                isSelected = selectedScreen == "screen2",
                onClick = {
                    selectedScreen = "screen2"
                    onScreenSelected("add")
                }
            )

            // Button 3
            AppBarButton(
                iconResId = R.drawable.search,
                text = " Who's In",
                isSelected = selectedScreen == "screen3",
                onClick = {
                    selectedScreen = "screen3"
                    onScreenSelected("whoin")
                }
            )

            // Button 3
            AppBarButton(
                iconResId = R.drawable.history,
                text = " History",
                isSelected = selectedScreen == "screen4",
                onClick = {
                    selectedScreen = "screen4"
                    onScreenSelected("history")
                }
            )


            // Button 2
            AppBarButton(
                iconResId = R.drawable.wallet,
                text = " Payments",
                isSelected = selectedScreen == "screen5",
                onClick = {
                    selectedScreen = "screen5"
                    onScreenSelected("payments")
                }
            )

            // Add more buttons as needed
        }
    }
}

@Composable
fun AppBarButton(iconResId: Int, text: String, isSelected: Boolean, onClick: () -> Unit) {
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
}