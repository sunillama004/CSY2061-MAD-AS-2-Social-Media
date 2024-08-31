package com.example.mylearn// mylearn package calling
import android.content.Intent// Intent calling
import android.os.Bundle// Bundle calling
import androidx.activity.ComponentActivity// ComponentActivity calling
import androidx.activity.compose.setContent// setContent calling
import androidx.compose.foundation.layout.*// layout calling
import androidx.compose.foundation.lazy.LazyColumn// LazyColumn calling
import androidx.compose.foundation.lazy.items// items calling
import androidx.compose.material.icons.Icons// Icons calling
import androidx.compose.material.icons.filled.*// filled calling
import androidx.compose.material3.*// material3 calling
import androidx.compose.runtime.*// runtime calling
import androidx.compose.ui.Alignment// Alignment calling
import androidx.compose.ui.Modifier// Modifier calling
import androidx.compose.ui.graphics.Color// Color calling
import androidx.compose.ui.platform.LocalContext// LocalContext calling
import androidx.compose.ui.text.font.FontWeight// FontWeight calling
import androidx.compose.ui.unit.dp// dp calling
import androidx.compose.ui.unit.sp// sp calling
import com.example.mylearn.entity.Mycomment// Mycomment calling
import com.example.mylearn.entity.Mypost// Mypost calling
import com.example.mylearn.repository.MycommentsRepositoryInterface// MycommentsRepositoryInterface calling
import com.example.mylearn.repository.MypostsRepositoryInterface// MypostsRepositoryInterface calling
import com.example.mylearn.ui.theme.MyLearnTheme// MyLearnTheme calling
import kotlinx.coroutines.launch// launch calling
class UserEditPostActivity : ComponentActivity() {
    private lateinit var mypostRepository: MypostsRepositoryInterface
    private lateinit var mycommentRepository: MycommentsRepositoryInterface
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mypostRepository = (application as MyRoomApplication).mydatabaseContainer.MypostsRepositoryInterface
        mycommentRepository = (application as MyRoomApplication).mydatabaseContainer.MycommentsRepositoryInterface
        setContent {
            MyLearnTheme {
                Column(modifier = Modifier.fillMaxSize()) {
                    val mycontext = LocalContext.current
                    val myRoomApplication = mycontext.applicationContext as MyRoomApplication
                    val myloggedInUser = myRoomApplication.myloggedInUser
                    val myuserId = myloggedInUser?.userid ?: -1

                    MyHeaderViews(
                        titles = "Customize My Post",
                        myleftButton = { },
                        myuserPosts = { },
                        userId = myuserId
                    )
                    Spacer(modifier = Modifier.height(18.5.dp))
                    MyMainScreenUsers(
                        mypostRepository = mypostRepository,
                        mycommentRepository = mycommentRepository,
                        myaddComments = { post -> myaddComments(post) }
                    )
                }
            }
        }
    }
    private fun myaddComments(post: Mypost) {
        val myintent = Intent(this, AddCommentActivity::class.java)
        myintent.putExtra("postId", post.id_post)
        startActivity(myintent)
    }
}
@Composable
fun MyMainScreenUsers(
    mypostRepository: MypostsRepositoryInterface,
    mycommentRepository: MycommentsRepositoryInterface,
    myaddComments: (Mypost) -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val myRoomApplication = context.applicationContext as MyRoomApplication
    val myloggedInUser = myRoomApplication.myloggedInUser
    val uemail = myloggedInUser?.u_email ?: ""
    var myposts by remember { mutableStateOf<List<Mypost>>(emptyList()) }
    var mycomments by remember { mutableStateOf<List<Mycomment>>(emptyList()) }
    val mylikedPosts by remember { mutableStateOf(mutableSetOf<Int>()) }
    LaunchedEffect(uemail) {
        try {
            mypostRepository.getMyPostsByEmail(uemail).collect { myfetchedPosts ->
                myposts = myfetchedPosts
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    LaunchedEffect(Unit) {
        // fetching comments
        mycommentRepository.getAllMycommentsStream().collect { mycommentList ->
            mycomments = mycommentList
        }
    }
    MyHomeScreenUsers(
        myposts = myposts,
        mycomments = mycomments,
        mylikedPosts = mylikedPosts,
        whenDeletes = {
            scope.launch {
                mypostRepository.deleteMypost(it)
            }
        },
        whenLikes = { post ->
            val isLiked = mylikedPosts.contains(post.id_post)
            val updatedPost = if (isLiked) {
                post.copy(post_like = post.post_like - 1)
            } else {
                post.copy(post_like = post.post_like + 1)
            }
            scope.launch {
                mypostRepository.updateMypost(updatedPost)
                if (isLiked) {
                    mylikedPosts.remove(post.id_post)
                } else {
                    mylikedPosts.add(post.id_post)
                }
            }
        },
        myaddComments = myaddComments
    )
}
@Composable
fun MyHomeScreenUsers(
    myposts: List<Mypost>,
    mycomments: List<Mycomment>,
    mylikedPosts: Set<Int>,
    whenDeletes: (Mypost) -> Unit,
    whenLikes: (Mypost) -> Unit,
    myaddComments: (Mypost) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.5.dp)
    ) {
        if (myposts.isEmpty()) {
            Text(
                text = "No posts yet, please add some posts",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 18.sp,
                color = Color.Gray
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(18.5.dp),
                verticalArrangement = Arrangement.spacedBy(10.5.dp)
            ) {
                items(myposts) { post ->
                    val postComments = mycomments.filter { it.id_post == post.id_post }
                    MyPostItemUsers(
                        mypost = post,
                        mycomments = postComments,
                        myisLiked = mylikedPosts.contains(post.id_post),
                        whenDeletes = whenDeletes,
                        whenLikes = whenLikes,
                        myaddComment = myaddComments
                    )
                }
            }
        }
    }
}
//(Stevdza-San, 2020)
@Composable
fun MyPostItemUsers(
    mypost: Mypost,
    mycomments: List<Mycomment>,
    myisLiked: Boolean,
    whenDeletes: (Mypost) -> Unit,
    whenLikes: (Mypost) -> Unit,
    myaddComment: (Mypost) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.5.dp),
        elevation = CardDefaults.cardElevation(6.5.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.5.dp),
            verticalArrangement = Arrangement.spacedBy(18.5.dp) // Space between elements
        ) {
            Text(text = "Posted by: ${mypost.postby_name}", fontWeight = FontWeight.Bold)
            Text(text = mypost.post_content)
            // button rows for Edit, Delete and Like
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
                            tint = if (myisLiked) Color.Red else Color.Gray
                        )
                    }
                    Text(text = "Likes: ${mypost.post_like}", modifier = Modifier.align(Alignment.CenterVertically))
                }
                Row {
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = { whenDeletes(mypost) }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
            // Comments List
            Text(text = "Comments:", fontWeight = FontWeight.Bold)
            if (mycomments.isEmpty()) {
                Text(text = "No comments yet")
            } else {
                Column {
                    mycomments.forEach { comment ->
                        Text(text = "${comment.cmtby_name}: ${comment.cmt_content}", modifier = Modifier.padding(vertical = 6.5.dp))
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
//Stevdza-San (2020) ROOM Database - #4 Update Data | Android Studio Tutorial. [Online]. Available from: https://www.youtube.com/watch?v=5rfBU75sguk&list=PLSrm9z4zp4mEPOfZNV9O-crOhoMa0G2-o&index=4 [Accessed 11 August 2024].