package com.example.mylearn // mylearn package calling
import android.content.Intent // Intent calling
import android.os.Bundle // Bundle calling
import androidx.activity.ComponentActivity // ComponentActivity calling
import androidx.activity.compose.setContent // setContent calling
import androidx.compose.foundation.background // background calling
import androidx.compose.foundation.layout.* // all layout calling
import androidx.compose.foundation.lazy.LazyColumn // LazyColumn calling
import androidx.compose.foundation.lazy.items // items calling
import androidx.compose.material.icons.Icons // Icons calling
import androidx.compose.material.icons.filled.Person // Person calling
import androidx.compose.material3.* // all material3 calling
import androidx.compose.runtime.* // all runtime calling
import androidx.compose.ui.Alignment // Alignment calling
import androidx.compose.ui.Modifier // Modifier calling
import androidx.compose.ui.graphics.Color // Color calling
import androidx.compose.ui.platform.LocalContext // LocalContext calling
import androidx.compose.ui.text.font.FontWeight // FontWeight calling
import androidx.compose.ui.unit.dp // dp calling
import androidx.compose.ui.unit.sp // sp calling
import com.example.mylearn.entity.Mycomment // Mycomment calling
import com.example.mylearn.entity.Mypost // Mypost calling
import com.example.mylearn.entity.Myuser // Myuser calling
import com.example.mylearn.repository.MycommentsRepositoryInterface // MycommentsRepositoryInterface calling
import com.example.mylearn.repository.MypostsRepositoryInterface // MypostsRepositoryInterface calling
import com.example.mylearn.repository.MyusersRepositoryInterface // MyusersRepositoryInterface calling
import com.example.mylearn.ui.theme.MyLearnTheme // MyLearnTheme calling
import kotlinx.coroutines.launch // coroutines launch calling
class AdminDashboardActivity : ComponentActivity() {
    private lateinit var mypostRepository: MypostsRepositoryInterface
    private lateinit var mycommentRepository: MycommentsRepositoryInterface
    private lateinit var myusersRepository: MyusersRepositoryInterface
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mypostRepository = (application as MyRoomApplication).mydatabaseContainer.MypostsRepositoryInterface
        mycommentRepository = (application as MyRoomApplication).mydatabaseContainer.MycommentsRepositoryInterface
        myusersRepository = (application as MyRoomApplication).mydatabaseContainer.MyusersRepositoryInterface
        setContent {
            MyLearnTheme {
                MyMainScreenAdmins(
                    mypostRepository = mypostRepository,
                    mycommentRepository = mycommentRepository,
                    myusersRepository = myusersRepository,
                    myuserPosts = { myuserPosts() }
                )
            }
        }
    }
    private fun myuserPosts() {
        val intent = Intent(this, AdminManagePostActivity::class.java)
        startActivity(intent)
    }
}
//(Jeff Atwood and Joel Spolsky, 2008)
//(Stevdza-San, 2020)
@Composable
fun MyMainScreenAdmins(
    mypostRepository: MypostsRepositoryInterface,
    myusersRepository: MyusersRepositoryInterface,
    mycommentRepository: MycommentsRepositoryInterface,
    myuserPosts: () -> Unit
) {
    val myscope = rememberCoroutineScope()
    var myposts by remember { mutableStateOf<List<Mypost>>(emptyList()) }
    var myusers by remember { mutableStateOf<Myuser?>(null) }
    var mycomments by remember { mutableStateOf<List<Mycomment>>(emptyList()) }
    val mycontext = LocalContext.current
    val myroomApplication = mycontext.applicationContext as MyRoomApplication
    val myloggedInUser = myroomApplication.myloggedInUser
    val ismyAdminLoggedIn = myroomApplication.ismyAdminLoggedIn
    val myuserid = myloggedInUser?.userid ?: -1
    LaunchedEffect(Unit) {
        // fetching posts
        myscope.launch {
            mypostRepository.getAllMypostsStream().collect { mypostList ->
                myposts = mypostList
            }
        }
        // fetching comments
        myscope.launch {
            mycommentRepository.getAllMycommentsStream().collect { mycommentList ->
                mycomments = mycommentList
            }
        }
    }
    LaunchedEffect(myuserid) {
        try {
            myusersRepository.getMyuserStream(myuserid).collect { myfetchedUser ->
                myusers = myfetchedUser
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    MyAdminsContent(
        myposts = myposts,
        mycomments = mycomments,
        myuserPosts = myuserPosts,
        ismyAdminLoggedIn = ismyAdminLoggedIn
    )
}
@Composable
fun MyAdminsContent(
    myposts: List<Mypost>,
    mycomments: List<Mycomment>,
    myuserPosts: () -> Unit,
    ismyAdminLoggedIn: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AdminsHeaderViews(
            titles = "Admin Dashboard",
            ismyAdminLoggedIn = ismyAdminLoggedIn
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(myposts) { mypost ->
                val mypostComments = mycomments.filter { it.id_post == mypost.id_post }
                val mypostLikesCount = mycomments.count { it.id_post == mypost.id_post } // Assuming likes are stored in comments
                MyPostItem(
                    mypost = mypost,
                    mycomments = mypostComments,
                    mylikedPostsCount = mypostLikesCount
                )
            }
        }
        if (ismyAdminLoggedIn) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { myuserPosts() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("View/Edit My Posts")
            }
        }
    }
}
@Composable
fun MyPostItem(
    mypost: Mypost,
    mycomments: List<Mycomment>,
    mylikedPostsCount: Int
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
            // counting likes showing
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
        }
    }
}
@Composable
fun AdminsHeaderViews(
    titles: String,
    ismyAdminLoggedIn: Boolean
) {
    val mycontext = LocalContext.current
    mycontext.applicationContext as MyRoomApplication
    var myexpanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = titles,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            Box {
                IconButton(onClick = { myexpanded = true }) {
                    Icon(imageVector = Icons.Default.Person, contentDescription = "Profile", tint = Color.White)
                }
                //(KB CODER, 2022)
                DropdownMenu(
                    expanded = myexpanded,
                    onDismissRequest = { myexpanded = false }
                ) {
                    if (ismyAdminLoggedIn) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Person, contentDescription = "User Icon", tint = Color.Gray)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "admin", fontWeight = FontWeight.Bold)
                        }
                        DropdownMenuItem(
                            text = { Text("Customize Students Account") },
                            onClick = {
                                myexpanded = false
                                val intent = Intent(mycontext, AdminManageActivity::class.java)
                                mycontext.startActivity(intent)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Add Students Account") },
                            onClick = {
                                myexpanded = false
                                val intent = Intent(mycontext, MyregisterActivity::class.java)
                                mycontext.startActivity(intent)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Customize All Posts") },
                            onClick = {
                                val intent = Intent(mycontext, AdminManagePostActivity::class.java)
                                mycontext.startActivity(intent)
                                myexpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Logout") },
                            onClick = {
                                (mycontext.applicationContext as MyRoomApplication).do_logout()
                                myexpanded = false
                            }
                        )
                    } else {
                        Text("Not logged in", modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}
//KB CODER (2022) Dropdown Menu With Jetpack Compose in Android Studio | Kotlin | Jetpack Compose | Android Tutorials. [Online]. Available from: https://www.youtube.com/watch?v=7A-Wo-TQ1eE [Accessed 13 August 2024].
//Stevdza-San (2020) ROOM Database - #2 Insert Data | Android Studio Tutorial. [Online]. Available from: https://www.youtube.com/watch?v=UBCAWfztTrQ&list=PLSrm9z4zp4mEPOfZNV9O-crOhoMa0G2-o&index=3 [Accessed 10 August 2024].
//Stevdza-San (2020) ROOM Database - #4 Update Data | Android Studio Tutorial. [Online]. Available from: https://www.youtube.com/watch?v=5rfBU75sguk&list=PLSrm9z4zp4mEPOfZNV9O-crOhoMa0G2-o&index=4 [Accessed 11 August 2024].
// Jeff Atwood and Joel Spolsky (2008) stackoverflow. [Online]. Available from:https://stackoverflow.com [Accessed 10 August 2024].