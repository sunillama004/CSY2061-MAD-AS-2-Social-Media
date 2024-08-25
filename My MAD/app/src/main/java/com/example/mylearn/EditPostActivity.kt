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
import com.example.mylearn.repository.MypostsRepositoryInterface// MypostsRepositoryInterface calling
import kotlinx.coroutines.launch// launch calling
class EditPostActivity : ComponentActivity() {
    private lateinit var mypostRepository: MypostsRepositoryInterface
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mypostRepository = (application as MyRoomApplication).mydatabaseContainer.MypostsRepositoryInterface
        val mypostId = intent.getIntExtra("postId", -1)
        if (mypostId == -1) {
            Toast.makeText(this, "Invalid post ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        setContent {
            MyLearnTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MyEditPostScreens(mypostRepository, mypostId) {
                        Toast.makeText(this, "Post updated", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }
}
//(KB CODER, 2022)
@Composable
fun MyEditPostScreens(postRepository: MypostsRepositoryInterface, mypostId: Int, whenPostUpdated: () -> Unit) {
    var mypost by remember { mutableStateOf<Mypost?>(null) }
    var mycontent by remember { mutableStateOf("") }
    val myscope = rememberCoroutineScope()
    LaunchedEffect(mypostId) {
        postRepository.getMypostStream(mypostId).collect { myfetchedPost ->
            mypost = myfetchedPost
            myfetchedPost?.let {
                mycontent = it.post_content
            }
        }
    }
    if (mypost == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
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
                val myupdatedPost = mypost!!.copy(post_content = mycontent)
                myscope.launch {
                    postRepository.updateMypost(myupdatedPost)
                    whenPostUpdated()
                }
            }) {
                Text("Update Post")
            }
        }
    }
}
//KB CODER (2022) Dropdown Menu With Jetpack Compose in Android Studio | Kotlin | Jetpack Compose | Android Tutorials. [Online]. Available from: https://www.youtube.com/watch?v=7A-Wo-TQ1eE [Accessed 12 August 2024].