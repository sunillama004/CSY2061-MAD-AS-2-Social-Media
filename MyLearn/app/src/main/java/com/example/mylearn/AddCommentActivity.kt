package com.example.mylearn // mylearn package calling
import android.os.Bundle // Bundle calling
import android.widget.Toast // Toast calling
import androidx.activity.ComponentActivity // ComponentActivity calling
import androidx.activity.compose.setContent // setContent calling
import androidx.compose.foundation.layout.* // layout calling
import androidx.compose.material3.* // all material3 calling
import androidx.compose.runtime.* // all runtime calling
import androidx.compose.ui.Alignment // Alignment calling
import androidx.compose.ui.Modifier // Modifier calling
import androidx.compose.ui.platform.LocalContext // LocalContext calling
import androidx.compose.ui.unit.dp // unit.dp calling
import com.example.mylearn.entity.Mycomment // Mycomment calling
import com.example.mylearn.repository.MycommentsRepositoryInterface // MycommentsRepositoryInterface calling
import com.example.mylearn.ui.theme.MyLearnTheme // MyLearnTheme calling
import kotlinx.coroutines.launch // coroutines calling
class AddCommentActivity : ComponentActivity() {
    private lateinit var mycommentRepository: MycommentsRepositoryInterface
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mycommentRepository = (application as MyRoomApplication).mydatabaseContainer.MycommentsRepositoryInterface
        val mypostId = intent.getIntExtra("postId", -1)
        if (mypostId == -1) {
            Toast.makeText(this, "Invalid post ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        setContent {
            MyLearnTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MyAddCommentScreen(mycommentRepository, mypostId) {
                        Toast.makeText(this, "Comment added", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }
}
//(Stevdza-San, 2020)
@Composable
fun MyAddCommentScreen(
    mycommentRepository: MycommentsRepositoryInterface,
    mypostId: Int,
    whenCommentAdded: () -> Unit
) {
    var mycommentContent by remember { mutableStateOf("") }
    val myscope = rememberCoroutineScope()
    val mycontext = LocalContext.current // retrieving the context
    val myuserName = "Default User" // retrieving dynamically username

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = mycommentContent,
            onValueChange = { mycommentContent = it },
            label = { Text("Comment") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(18.5.dp))
        Button(onClick = {
            val newComment = Mycomment(
                cmt_content = mycommentContent,
                cmtby_name = myuserName, // Adding new name
                id_post = mypostId
            )
            myscope.launch {
                try {
                    mycommentRepository.insertMycomment(newComment)
                    whenCommentAdded()
                } catch (e: Exception) {
                    // Using context
                    Toast.makeText(mycontext, "Failed to add comment: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }) {
            Text("Add Comment")
        }
    }
}
//Stevdza-San (2020) ROOM Database - #2 Insert Data | Android Studio Tutorial. [Online]. Available from: https://www.youtube.com/watch?v=UBCAWfztTrQ&list=PLSrm9z4zp4mEPOfZNV9O-crOhoMa0G2-o&index=3 [Accessed 10 August 2024].