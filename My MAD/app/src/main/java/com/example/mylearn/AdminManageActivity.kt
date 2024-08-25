package com.example.mylearn // mylearn package calling
import android.os.Bundle // Bundle calling
import androidx.activity.ComponentActivity // ComponentActivity calling
import androidx.activity.compose.setContent // setContent calling
import androidx.compose.foundation.layout.* // all layout calling
import androidx.compose.foundation.lazy.LazyColumn // LazyColumn calling
import androidx.compose.foundation.lazy.items // items calling
import androidx.compose.material3.* // all material3 calling
import androidx.compose.runtime.* // all runtime calling
import androidx.compose.ui.Modifier // Modifier calling
import androidx.compose.ui.platform.LocalContext // LocalContext calling
import androidx.compose.ui.text.font.FontWeight // FontWeight calling
import androidx.compose.ui.text.input.PasswordVisualTransformation // PasswordVisualTransformation calling
import androidx.compose.ui.unit.dp // dp calling
import com.example.mylearn.entity.Myuser // Myuser calling
import com.example.mylearn.repository.MyusersRepositoryInterface // MyusersRepositoryInterface calling
import com.example.mylearn.ui.theme.MyLearnTheme // MyLearnTheme calling
import kotlinx.coroutines.launch // launch calling
class AdminManageActivity : ComponentActivity() {
    private lateinit var myusersRepository: MyusersRepositoryInterface
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myusersRepository = (application as MyRoomApplication).mydatabaseContainer.MyusersRepositoryInterface
        setContent {
            MyLearnTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val mycontext = LocalContext.current
                    val myRoomApplication = mycontext.applicationContext as MyRoomApplication
                    val ismyAdminLoggedIn = myRoomApplication.ismyAdminLoggedIn
                    val myloggedInUser = myRoomApplication.myloggedInUser
                    val userId = myloggedInUser?.userid ?: -1
                    Column(modifier = Modifier.fillMaxSize()) {
                        AdminsHeaderViews(
                            titles = "Admin-Manage Users",
                            ismyAdminLoggedIn = ismyAdminLoggedIn
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        MyUsersListScreen(myusersRepository)
                    }
                }
            }
        }
    }
}
//(Stevdza-San, 2020)
@Composable
fun MyUsersListScreen(usersRepository: MyusersRepositoryInterface) {
    val myscope = rememberCoroutineScope()
    var myusers by remember { mutableStateOf<List<Myuser>>(emptyList()) }
    var myuserToEdit by remember { mutableStateOf<Myuser?>(null) }
    var myuserToDelete by remember { mutableStateOf<Myuser?>(null) }
    var openShowDeleteDialog by remember { mutableStateOf(false) }
    var openShowEditDialog by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        myscope.launch {
            usersRepository.getAllMyusersStream().collect { myuserList ->
                myusers = myuserList
            }
        }
    }
    if (openShowEditDialog) {
        myuserToEdit?.let { newuser ->
            MyEditUserDialog(newuser, { newupdatedUser ->
                myscope.launch {
                    usersRepository.updateMyuser(newupdatedUser)
                    openShowEditDialog = false
                }
            }, { openShowEditDialog = false })
        }
    }
    if (openShowDeleteDialog) {
        myuserToDelete?.let { newuser ->
            MyDeleteUserDialog({
                myscope.launch {
                    usersRepository.deleteMyuser(newuser)
                    openShowDeleteDialog = false
                }
            }, { openShowDeleteDialog = false })
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(myusers) { myuser ->
                MyUserItem(
                    myuser = myuser,
                    whenEdit = { myuserToEdit = myuser; openShowEditDialog = true },
                    whenDelete = { myuserToDelete = myuser; openShowDeleteDialog = true }
                )
            }
        }
    }
}
@Composable
fun MyUserItem(myuser: Myuser, whenEdit: () -> Unit, whenDelete: () -> Unit) {
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
            Text(text = "User ID: ${myuser.userid}", fontWeight = FontWeight.Bold)
            Text(text = "Name: ${myuser.u_name}")
            Text(text = "Email: ${myuser.u_email}")
            Text(text = "Date of Birth: ${myuser.u_dob}")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = whenEdit) {
                    Text(text = "Edit")
                }
                Button(onClick = whenDelete) {
                    Text(text = "Delete")
                }
            }
        }
    }
}
//(Stevdza-San, 2020)
@Composable
fun MyEditUserDialog(myuser: Myuser, whenSave: (Myuser) -> Unit, whenDismiss: () -> Unit) {
    var myname by remember { mutableStateOf(myuser.u_name) }
    var myemail by remember { mutableStateOf(myuser.u_email) }
    var mydob by remember { mutableStateOf(myuser.u_dob) }
    var mypassword by remember { mutableStateOf(myuser.u_password) }
    AlertDialog(
        onDismissRequest = whenDismiss,
        title = { Text(text = "Edit User") },
        text = {
            Column {
                OutlinedTextField(
                    value = myname,
                    onValueChange = { myname = it },
                    label = { Text("Name") }
                )
                OutlinedTextField(
                    value = myemail,
                    onValueChange = { myemail = it },
                    label = { Text("Email") }
                )
                OutlinedTextField(
                    value = mydob,
                    onValueChange = { mydob = it },
                    label = { Text("Date of Birth") }
                )
                OutlinedTextField(
                    value = mypassword,
                    onValueChange = { mypassword = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                whenSave(myuser.copy(
                    u_name = myname,
                    u_email = myemail,
                    u_dob = mydob,
                    u_password = mypassword,
                    updated_date = System.currentTimeMillis()
                ))
            }) {
                Text(text = "Save")
            }
        },
        dismissButton = {
            Button(onClick = whenDismiss) {
                Text(text = "Cancel")
            }
        }
    )
}
@Composable
fun MyDeleteUserDialog(whenConfirm: () -> Unit, whenDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = whenDismiss,
        title = { Text(text = "Delete User") },
        text = { Text(text = "Are you sure you want to delete this user?") },
        confirmButton = {
            Button(onClick = whenConfirm) {
                Text(text = "Delete")
            }
        },
        dismissButton = {
            Button(onClick = whenDismiss) {
                Text(text = "Cancel")
            }
        }
    )
}
//Stevdza-San (2020) ROOM Database - #4 Update Data | Android Studio Tutorial. [Online]. Available from: https://www.youtube.com/watch?v=5rfBU75sguk&list=PLSrm9z4zp4mEPOfZNV9O-crOhoMa0G2-o&index=4 [Accessed 11 August 2024].