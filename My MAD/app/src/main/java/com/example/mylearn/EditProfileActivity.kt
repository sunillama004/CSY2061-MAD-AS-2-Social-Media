package com.example.mylearn// mylearn package calling
import android.os.Bundle// Bundle calling
import android.widget.Toast// Toast calling
import androidx.activity.ComponentActivity// ComponentActivity calling
import androidx.activity.compose.setContent// setContent calling
import androidx.compose.foundation.layout.*// layout calling
import androidx.compose.foundation.text.KeyboardOptions// KeyboardOptions calling
import androidx.compose.material3.*// material3 calling
import androidx.compose.runtime.*// runtime calling
import androidx.compose.ui.Alignment// Alignment calling
import androidx.compose.ui.Modifier// Modifier calling
import androidx.compose.ui.platform.LocalContext// LocalContext calling
import androidx.compose.ui.text.input.KeyboardType// KeyboardType calling
import androidx.compose.ui.text.input.PasswordVisualTransformation// PasswordVisualTransformation calling
import androidx.compose.ui.tooling.preview.Preview// Preview calling
import androidx.compose.ui.unit.dp// dp calling
import androidx.lifecycle.lifecycleScope// lifecycleScope calling
import com.example.mylearn.database.MyDatabaseDataContainer// MyMyDatabaseDataContainer calling
import com.example.mylearn.entity.Myuser// Myuser calling
import com.example.mylearn.ui.theme.MyLearnTheme// MyLearnTheme calling
import kotlinx.coroutines.launch// launch calling
class EditProfileActivity : ComponentActivity() {
    private lateinit var mydatabaseContainer: MyDatabaseDataContainer
    private var userId: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mydatabaseContainer = (application as MyRoomApplication).mydatabaseContainer
        userId = intent.getIntExtra("userid", -1)
        if (userId == -1) {
            Toast.makeText(this, "Invalid user ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        setContent {
            MyLearnTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyEditProfileScreen(userId, mydatabaseContainer) { myuser ->
                        myupdateUser(myuser)
                    }
                }
            }
        }
    }
    private fun myupdateUser(myuser: Myuser) {
        lifecycleScope.launch {
            try {
                mydatabaseContainer.MyusersRepositoryInterface.updateMyuser(myuser)
                runOnUiThread {
                    Toast.makeText(this@EditProfileActivity, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                }
                finish()
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@EditProfileActivity, "Error updating profile", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
//(Stevdza-San, 2020)
@Composable
fun MyEditProfileScreen(userId: Int, mydatabaseContainer: MyDatabaseDataContainer, myupdateAction: (Myuser) -> Unit) {
    var myuser by remember { mutableStateOf<Myuser?>(null) }
    LaunchedEffect(userId) {
        mydatabaseContainer.MyusersRepositoryInterface.getMyuserStream(userId).collect { myfetchedUser ->
            myuser = myfetchedUser
        }
    }
    myuser?.let { existingUser ->
        var newname by remember { mutableStateOf(existingUser.u_name) }
        var newpassword by remember { mutableStateOf(existingUser.u_password) }
        var newemail by remember { mutableStateOf(existingUser.u_email) }
        var newdob by remember { mutableStateOf(existingUser.u_dob) }
        var newnameError by remember { mutableStateOf<String?>(null) }
        var newpasswordError by remember { mutableStateOf<String?>(null) }
        var newemailError by remember { mutableStateOf<String?>(null) }
        var newdobError by remember { mutableStateOf<String?>(null) }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize()
            ) {
                OutlinedTextField(
                    value = newname,
                    onValueChange = { newname = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = newnameError != null,
                    supportingText = { newnameError?.let { Text(text = it, color = MaterialTheme.colorScheme.error) } }
                )
                OutlinedTextField(
                    value = newpassword,
                    onValueChange = { newpassword = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    isError = newpasswordError != null,
                    supportingText = { newpasswordError?.let { Text(text = it, color = MaterialTheme.colorScheme.error) } }
                )
                OutlinedTextField(
                    value = newemail,
                    onValueChange = { newemail = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    isError = newemailError != null,
                    supportingText = { newemailError?.let { Text(text = it, color = MaterialTheme.colorScheme.error) } }
                )
                OutlinedTextField(
                    value = newdob,
                    onValueChange = {
                        newdob = it
                        newdobError = if (isMyValidDate(it)) null else "Invalid date format"
                    },
                    label = { Text("Date of Birth (yyyy-MM-dd)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth(),
                    isError = newdobError != null,
                    supportingText = { newdobError?.let { Text(text = it, color = MaterialTheme.colorScheme.error) } }
                )
                Button(
                    onClick = {
                        val checkvalidName = newname.isNotBlank()
                        val checkvalidPassword = newpassword.isNotBlank()
                        val checkvalidEmail = newemail.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(newemail).matches()
                        val checkvalidDob = isMyValidDate(newdob)

                        newnameError = if (checkvalidName) null else "Name cannot be empty"
                        newpasswordError = if (checkvalidPassword) null else "Password cannot be empty"
                        newemailError = if (checkvalidEmail) null else "Invalid email format"
                        newdobError = if (checkvalidDob) null else "Invalid date format"

                        if (checkvalidName && checkvalidPassword && checkvalidEmail && checkvalidDob) {
                            val updatedUser = existingUser.copy(
                                u_name = newname,
                                u_password = newpassword,
                                u_email = newemail,
                                u_dob = newdob,
                                updated_date = System.currentTimeMillis()
                            )
                            myupdateAction(updatedUser)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Update Profile")
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    MyLearnTheme {
        // data for preview
        MyEditProfileScreen(userId = 1, mydatabaseContainer = MyDatabaseDataContainer(LocalContext.current)) { myuser ->
        }
    }
}
//KB CODER (2022) Dropdown Menu With Jetpack Compose in Android Studio | Kotlin | Jetpack Compose | Android Tutorials. [Online]. Available from: https://www.youtube.com/watch?v=7A-Wo-TQ1eE [Accessed 12 August 2024].