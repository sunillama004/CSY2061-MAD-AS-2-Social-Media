package com.example.mylearn // mylearn package calling
import android.content.Intent // Intent calling
import android.os.Bundle // Bundle calling
import androidx.activity.ComponentActivity // ComponentActivity calling
import androidx.activity.compose.setContent // setContent calling
import androidx.compose.foundation.layout.* // layout calling
import androidx.compose.foundation.lazy.LazyColumn // LazyColumn calling
import androidx.compose.foundation.lazy.items // items calling
import androidx.compose.material3.* // material3 calling
import androidx.compose.runtime.* // runtime calling
import androidx.compose.ui.Modifier // Modifier calling
import androidx.compose.ui.platform.LocalContext // LocalContext calling
import androidx.compose.ui.text.font.FontWeight // FontWeight calling
import androidx.compose.ui.unit.dp // dp calling
import com.example.mylearn.entity.Mycomment // Mycomment calling
import com.example.mylearn.entity.Mypost // Mypost calling
import com.example.mylearn.repository.MycommentsRepositoryInterface // MycommentsRepositoryInterface calling
import com.example.mylearn.repository.MypostsRepositoryInterface // MypostsRepositoryInterface calling
import com.example.mylearn.ui.theme.MyLearnTheme // MyLearnTheme calling
import kotlinx.coroutines.launch // launch calling
class AdminManagePostActivity : ComponentActivity() {
    private lateinit var mypostRepository: MypostsRepositoryInterface
    private lateinit var mycommentRepository: MycommentsRepositoryInterface
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mypostRepository = (application as MyRoomApplication).mydatabaseContainer.MypostsRepositoryInterface
        mycommentRepository = (application as MyRoomApplication).mydatabaseContainer.MycommentsRepositoryInterface
        setContent {
            MyLearnTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val mycontext = LocalContext.current
                    val myRoomApplication = mycontext.applicationContext as MyRoomApplication
                    val ismyAdminLoggedIn = myRoomApplication.ismyAdminLoggedIn
                    Column(modifier = Modifier.fillMaxSize()) {
                        AdminsHeaderViews(
                            titles = "Admin-Manage User Posts",
                            ismyAdminLoggedIn = ismyAdminLoggedIn
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        MainScreenManagePosts(
                            mypostRepository = mypostRepository,
                            mycommentRepository = mycommentRepository,
                            myeditPosts = { mypost -> myeditPosts(mypost) }
                        )
                    }
                }
            }
        }
    }
    private fun myeditPosts(mypost: Mypost) {
        val intent = Intent(this, EditPostActivity::class.java)
        intent.putExtra("postId", mypost.id_post)
        startActivity(intent)
    }
}
//(Stevdza-San, 2020)
@Composable
fun MainScreenManagePosts(
    mypostRepository: MypostsRepositoryInterface,
    mycommentRepository: MycommentsRepositoryInterface,
    myeditPosts: (Mypost) -> Unit
) {
    val myscope = rememberCoroutineScope()
    var myposts by remember { mutableStateOf<List<Mypost>>(emptyList()) }
    var mycomments by remember { mutableStateOf<List<Mycomment>>(emptyList()) }
    LaunchedEffect(Unit) {
        myscope.launch {
            mypostRepository.getAllMypostsStream().collect { mypostList ->
                myposts = mypostList
            }
        }
        // fetching all data
        myscope.launch {
            mycommentRepository.getAllMycommentsStream().collect { mycommentList ->
                mycomments = mycommentList
            }
        }
    }
    AdminsContentManagePosts(
        myposts = myposts,
        mycomments = mycomments,
        myeditPosts = myeditPosts,
        mydeletePost = { newpost ->
            myscope.launch {
                mypostRepository.deleteMypost(newpost)
            }
        }
    )
}
@Composable
fun AdminsContentManagePosts(
    myposts: List<Mypost>,
    mycomments: List<Mycomment>,
    myeditPosts: (Mypost) -> Unit,
    mydeletePost: (Mypost) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(myposts) { mypost ->
                val mypostComments = mycomments.filter { it.id_post == mypost.id_post }
                val mypostLikesCount = mycomments.count { it.id_post == mypost.id_post }
                MyPostItemManagePosts(
                    mypost = mypost,
                    mycomments = mypostComments,
                    mylikedPostsCount = mypostLikesCount,
                    myeditPost = { myeditPosts(mypost) },
                    mydeletePost = { mydeletePost(mypost) }
                )
            }
        }
    }
}
@Composable
fun MyPostItemManagePosts(
    mypost: Mypost,
    mycomments: List<Mycomment>,
    mylikedPostsCount: Int,
    myeditPost: () -> Unit,
    mydeletePost: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Posted by: ${mypost.postby_name}", fontWeight = FontWeight.Bold)
            Text(text = mypost.post_content)
            // showing count likes
            Text(text = "Liked by $mylikedPostsCount people", style = MaterialTheme.typography.bodySmall)
            // showing comments
            Text(text = "Comments:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
            if (mycomments.isEmpty()) {
                Text(text = "No comments yet")
            } else {
                Column {
                    mycomments.forEach { comment ->
                        Text(text = "${comment.cmtby_name}: ${comment.cmt_content}", modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = myeditPost) {
                    Text("Edit Post")
                }
                Button(onClick = mydeletePost) {
                    Text("Delete Post")
                }
            }
        }
    }
}
//Stevdza-San (2020) ROOM Database - #4 Update Data | Android Studio Tutorial. [Online]. Available from: https://www.youtube.com/watch?v=5rfBU75sguk&list=PLSrm9z4zp4mEPOfZNV9O-crOhoMa0G2-o&index=4 [Accessed 11 August 2024].