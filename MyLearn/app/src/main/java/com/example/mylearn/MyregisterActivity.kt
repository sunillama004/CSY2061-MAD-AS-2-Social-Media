package com.example.mylearn// mylearn package calling
import android.content.Intent// Intent calling
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
import com.example.mylearn.repository.MypostsRepositoryInterface// MypostsRepositoryInterface calling
import com.example.mylearn.ui.theme.MyLearnTheme// MyLearnTheme calling
import kotlinx.coroutines.launch// launch calling
import java.text.SimpleDateFormat// SimpleDateFormat calling
import java.util.Locale// Locale calling
//(Jeff Atwood and Joel Spolsky, 2008)
class MyregisterActivity : ComponentActivity() {
    private lateinit var mydatabaseContainer: MyDatabaseDataContainer
    private lateinit var mypostRepository: MypostsRepositoryInterface
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mydatabaseContainer = (application as MyRoomApplication).mydatabaseContainer
        mypostRepository = (application as MyRoomApplication).mydatabaseContainer.MypostsRepositoryInterface
        setContent {
            MyLearnTheme {
                val mycontext = LocalContext.current
                val myRoomApplication = mycontext.applicationContext as MyRoomApplication
                val ismyAdminLoggedIn = myRoomApplication.ismyAdminLoggedIn
                val myloggedInUser = myRoomApplication.myloggedInUser
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyRegisterScreen { name, password, email, dob ->
                        val newuser = Myuser(
                            u_name = name,
                            u_password = password,
                            u_email = email,
                            u_dob = dob,
                            created_date = System.currentTimeMillis(),
                            updated_date = System.currentTimeMillis()
                        )
                        myregisterUser(newuser, ismyAdminLoggedIn, myloggedInUser)
                    }
                }
            }
        }
    }
    private fun myregisterUser(newuser: Myuser, ismyAdminLoggedIn: Boolean, myloggedInUser: Myuser?) {
        lifecycleScope.launch {
            try {
                if (myloggedInUser == null) {
                    mydatabaseContainer.MyusersRepositoryInterface.insertMyuser(newuser)
                    runOnUiThread {
                        Toast.makeText(this@MyregisterActivity, "Registration added", Toast.LENGTH_SHORT).show()
                    }
                    // after successful registration goto MyLoginActivity
                    startActivity(Intent(this@MyregisterActivity, MyLoginActivity::class.java))
                    finish() // end activity
                } else if (ismyAdminLoggedIn) {
                    mydatabaseContainer.MyusersRepositoryInterface.insertMyuser(newuser)
                    runOnUiThread {
                        Toast.makeText(this@MyregisterActivity, "Registration added by Admin", Toast.LENGTH_SHORT).show()
                    }
                    startActivity(Intent(this@MyregisterActivity, AdminManageActivity::class.java))
                    finish() // end activity
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@MyregisterActivity, "Registration unsuccessful", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
@Composable
fun MyRegisterScreen(myregisterAction: (name: String, password: String, email: String, dob: String) -> Unit) {
    var newname by remember { mutableStateOf("") }
    var newpassword by remember { mutableStateOf("") }
    var newemail by remember { mutableStateOf("") }
    var newdob by remember { mutableStateOf("") }
    var newnameError by remember { mutableStateOf<String?>(null) }
    var newpasswordError by remember { mutableStateOf<String?>(null) }
    var newemailError by remember { mutableStateOf<String?>(null) }
    var newdobError by remember { mutableStateOf<String?>(null) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.5.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.5.dp),
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
                        myregisterAction(newname, newpassword, newemail, newdob)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Register")
            }
        }
    }
}
fun isMyValidDate(date: String): Boolean {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        format.isLenient = false
        format.parse(date) != null
    } catch (e: Exception) {
        false
    }
}
// Jeff Atwood and Joel Spolsky (2008) stackoverflow. [Online]. Available from:https://stackoverflow.com [Accessed 5 August 2024].