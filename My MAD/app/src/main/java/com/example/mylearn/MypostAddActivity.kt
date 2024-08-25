package com.example.mylearn// mylearn package calling
import android.os.Bundle// Bundle calling
import android.widget.Toast// Toast calling
import androidx.activity.ComponentActivity// ComponentActivity calling
import androidx.activity.compose.setContent// setContent calling
import androidx.compose.foundation.layout.*// layout calling
import androidx.compose.material3.*// material3 calling
import androidx.compose.runtime.*// runtime calling
import androidx.compose.ui.Alignment// Alignment calling
import androidx.compose.ui.Modifier// Modifier calling
import androidx.compose.ui.unit.dp// dp calling
import com.example.mylearn.ui.theme.MyLearnTheme// MyLearnTheme calling
import com.example.mylearn.entity.Mypost// Mypost calling
import com.example.mylearn.entity.Myuser// Myuser calling
import com.example.mylearn.repository.MypostsRepositoryInterface// MypostsRepositoryInterface calling
import kotlinx.coroutines.launch// launch calling
class MypostAddActivity : ComponentActivity() {
    private lateinit var mypostRepository: MypostsRepositoryInterface
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mypostRepository = (application as MyRoomApplication).mydatabaseContainer.MypostsRepositoryInterface
        setContent {
            MyLearnTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MyAddPostScreen(mypostRepository, (application as MyRoomApplication).myloggedInUser) {
                        Toast.makeText(this, "Post added", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }
}
//(Stevdza-San, 2020)
@Composable
fun MyAddPostScreen(postRepository: MypostsRepositoryInterface, myloggedInUser: Myuser?, whenPostAdded: () -> Unit) {
    var mycontent by remember { mutableStateOf("") }
    val myscope = rememberCoroutineScope()
    if (myloggedInUser == null) {
        Text("User not logged in")
        return
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = mycontent,
            onValueChange = { mycontent = it },
            label = { Text("Content") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val newPost = Mypost(
                postby_name = myloggedInUser.u_name,
                postby_email = myloggedInUser.u_email,
                post_content = mycontent,
                post_like = 0,
                post_dislike = 0
            )
            myscope.launch {
                postRepository.insertMypost(newPost)
                whenPostAdded()
            }
        }) {
            Text("Add Post")
        }
    }
}
//Stevdza-San (2020) ROOM Database - #2 Insert Data | Android Studio Tutorial. [Online]. Available from: https://www.youtube.com/watch?v=UBCAWfztTrQ&list=PLSrm9z4zp4mEPOfZNV9O-crOhoMa0G2-o&index=3 [Accessed 10 August 2024].