package com.example.mylearn// mylearn package calling
import android.content.Intent// Intent calling
import android.os.Bundle// Bundle calling
import androidx.activity.ComponentActivity// ComponentActivity calling
import androidx.activity.compose.setContent// setContent calling
import androidx.compose.foundation.background// background calling
import androidx.compose.foundation.layout.Arrangement// Arrangement calling
import androidx.compose.foundation.layout.Box// Box calling
import androidx.compose.foundation.layout.Column// Column calling
import androidx.compose.foundation.layout.PaddingValues// PaddingValues calling
import androidx.compose.foundation.layout.Row// Row calling
import androidx.compose.foundation.layout.Spacer// Spacer calling
import androidx.compose.foundation.layout.fillMaxSize// fillMaxSize calling
import androidx.compose.foundation.layout.fillMaxWidth// fillMaxWidth calling
import androidx.compose.foundation.layout.height// height calling
import androidx.compose.foundation.layout.padding// padding calling
import androidx.compose.foundation.layout.width// width calling
import androidx.compose.foundation.lazy.LazyColumn// LazyColumn calling
import androidx.compose.foundation.lazy.items// items calling
import androidx.compose.material.icons.Icons// Icons calling
import androidx.compose.material.icons.filled.Add// Add calling
import androidx.compose.material.icons.filled.Favorite// Favorite calling
import androidx.compose.material.icons.filled.Person// Person calling
import androidx.compose.material3.Button// Button calling
import androidx.compose.material3.Card// Card calling
import androidx.compose.material3.CardDefaults// CardDefaults calling
import androidx.compose.material3.DropdownMenu// DropdownMenu calling
import androidx.compose.material3.DropdownMenuItem// DropdownMenuItem calling
import androidx.compose.material3.Icon// Icon calling
import androidx.compose.material3.IconButton// IconButton calling
import androidx.compose.material3.MaterialTheme// MaterialTheme calling
import androidx.compose.material3.Text// Text calling
import androidx.compose.runtime.Composable// Composable calling
import androidx.compose.runtime.LaunchedEffect// LaunchedEffect calling
import androidx.compose.runtime.getValue// getValue calling
import androidx.compose.runtime.mutableStateOf// mutableStateOf calling
import androidx.compose.runtime.remember// remember calling
import androidx.compose.runtime.rememberCoroutineScope// rememberCoroutineScope calling
import androidx.compose.runtime.setValue// setValue calling
import androidx.compose.ui.Alignment// Alignment calling
import androidx.compose.ui.Modifier// Modifier calling
import androidx.compose.ui.graphics.Color// Color calling
import androidx.compose.ui.platform.LocalContext// LocalContext calling
import androidx.compose.ui.text.font.FontWeight// FontWeight calling
import androidx.compose.ui.unit.dp// dp calling
import androidx.compose.ui.unit.sp// sp calling
import com.example.mylearn.entity.Mycomment// Mycomment calling
import com.example.mylearn.entity.Mypost// Mypost calling
import com.example.mylearn.entity.Myuser// Myuser calling
import com.example.mylearn.repository.MycommentsRepositoryInterface// MycommentsRepositoryInterface calling
import com.example.mylearn.repository.MypostsRepositoryInterface// MypostsRepositoryInterface calling
import com.example.mylearn.repository.MyusersRepositoryInterface// MyusersRepositoryInterface calling
import com.example.mylearn.ui.theme.MyLearnTheme// MyLearnTheme calling
import kotlinx.coroutines.launch// launch calling
class AllPostActivity : ComponentActivity() {
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
                MyMainScreen(
                    mypostRepository = mypostRepository,
                    mycommentRepository = mycommentRepository,
                    myusersRepository = myusersRepository,
                    myaddPosts = { myaddPosts() },
                    myuserPosts = { myuserPosts() },
                    myaddComments = { post -> myaddComments(post) }
                )
            }
        }
    }
    private fun myaddComments(post: Mypost) {
        val intent = Intent(this, AddCommentActivity::class.java)
        intent.putExtra("postId", post.id_post)
        startActivity(intent)
    }
    private fun myaddPosts() {
        val intent = Intent(this, MypostAddActivity::class.java)
        startActivity(intent)
    }
    private fun myuserPosts() {
        val intent = Intent(this, UserEditPostActivity::class.java)
        startActivity(intent)
    }
}
//(Jeff Atwood and Joel Spolsky, 2008)
@Composable
fun MyMainScreen(
    mypostRepository: MypostsRepositoryInterface,
    myusersRepository: MyusersRepositoryInterface,
    mycommentRepository: MycommentsRepositoryInterface,
    myaddPosts: () -> Unit,
    myuserPosts: () -> Unit,
    myaddComments: (Mypost) -> Unit
) {
    val myscope = rememberCoroutineScope()
    var myposts by remember { mutableStateOf<List<Mypost>>(emptyList()) }
    var myusers by remember { mutableStateOf<Myuser?>(null) }
    var mycomments by remember { mutableStateOf<List<Mycomment>>(emptyList()) }
    val mylikedPosts by remember { mutableStateOf(mutableSetOf<Int>()) }
    val mycontext = LocalContext.current
    val myRoomApplication = mycontext.applicationContext as MyRoomApplication
    val myloggedInUser = myRoomApplication.myloggedInUser
    val myuserid = myloggedInUser?.userid ?: -1
    LaunchedEffect(Unit) {
        // Collecting posts
        myscope.launch {
            mypostRepository.getAllMypostsStream().collect { mypostList ->
                myposts = mypostList
            }
        }
        // fetching all comments
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
    MyHomeScreens(
        myposts = myposts,
        mycomments = mycomments,
        mylikedPosts = mylikedPosts,
        whenLikes = { post ->
            val ifLiked = mylikedPosts.contains(post.id_post)
            val whenUpdatedPost = if (ifLiked) {
                post.copy(post_like = post.post_like - 1)
            } else {
                post.copy(post_like = post.post_like + 1)
            }
            myscope.launch {
                mypostRepository.updateMypost(whenUpdatedPost)
                if (ifLiked) {
                    mylikedPosts.remove(post.id_post)
                } else {
                    mylikedPosts.add(post.id_post)
                }
            }
        },
        myaddComments = myaddComments,
        myaddPosts = myaddPosts,
        myuserPosts = myuserPosts,
        myuserId = myuserid
    )
}
@Composable
fun MyHomeScreens(
    myposts: List<Mypost>,
    mycomments: List<Mycomment>,
    mylikedPosts: Set<Int>,
    whenLikes: (Mypost) -> Unit,
    myaddComments: (Mypost) -> Unit,
    myaddPosts: () -> Unit,
    myuserPosts: () -> Unit,
    myuserId: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        MyHeaderViews(
            titles = "Post Lists",
            myleftButton = myaddPosts,
            myuserPosts = myuserPosts,
            userId = myuserId
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(myposts) { newpost ->
                val mypostComments = mycomments.filter { it.id_post == newpost.id_post }
                MyPostItems(
                    mypost = newpost,
                    mycomments = mypostComments,
                    ifLiked = mylikedPosts.contains(newpost.id_post),
                    whenLikes = whenLikes,
                    myaddComment = myaddComments
                )
            }
        }
    }
}
//(Stevdza-San, 2020)
@Composable
fun MyPostItems(
    mypost: Mypost,
    mycomments: List<Mycomment>,
    ifLiked: Boolean,
    whenLikes: (Mypost) -> Unit,
    myaddComment: (Mypost) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Posted by: ${mypost.postby_name}", fontWeight = FontWeight.Bold)
            Text(text = mypost.post_content)

            // Actions Row (Like)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    IconButton(onClick = { whenLikes(mypost) }) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Like",
                            tint = if (ifLiked) Color.Red else Color.Gray
                        )
                    }
                    Text(text = "Likes: ${mypost.post_like}", modifier = Modifier.align(Alignment.CenterVertically))
                }
            }
            // Comments List
            Text(text = "Comments:", fontWeight = FontWeight.Bold)
            if (mycomments.isEmpty()) {
                Text(text = "No comments yet")
            } else {
                Column {
                    mycomments.forEach { comment ->
                        Text(text = "${comment.cmtby_name}: ${comment.cmt_content}", modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }
            // Add Comment Button
            Button(onClick = { myaddComment(mypost) }) {
                Text("Add Comment")
            }
        }
    }
}
@Composable
fun MyHeaderViews(
    titles: String,
    myleftButton: () -> Unit,
    myuserPosts: () -> Unit,
    userId: Int
) {
    val mycontext = LocalContext.current
    val myRoomApplication = mycontext.applicationContext as MyRoomApplication
    val myloggedInUser = myRoomApplication.myloggedInUser
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
            IconButton(onClick = { myleftButton() }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add", tint = Color.White)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = titles,
                color = Color.White,
                fontSize = 20.sp
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
                    if (myloggedInUser != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Person, contentDescription = "User Icon", tint = Color.Gray)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = myloggedInUser.u_name, fontWeight = FontWeight.Bold)
                        }
                        DropdownMenuItem(
                            text = { Text("Edit Profile") },
                            onClick = {
                                myexpanded = false
                                val intent = Intent(mycontext, EditProfileActivity::class.java)
                                intent.putExtra("userid", userId)
                                mycontext.startActivity(intent)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Customize My Posts") },
                            onClick = {
                                myuserPosts()
                                myexpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Logout") },
                            onClick = {
                                (mycontext.applicationContext as MyRoomApplication).do_logout()
                                val intent = Intent(mycontext, MyLoginActivity::class.java)
                                mycontext.startActivity(intent)
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