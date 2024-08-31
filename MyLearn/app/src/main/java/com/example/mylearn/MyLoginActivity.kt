package com.example.mylearn// mylearn package calling
import android.content.Intent// Intent calling
import android.os.Bundle// Bundle calling
import android.widget.Toast// Toast calling
import androidx.activity.ComponentActivity// ComponentActivity calling
import androidx.activity.compose.setContent// setContent calling
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*// layout calling
import androidx.compose.foundation.text.KeyboardOptions// KeyboardOptions calling
import androidx.compose.material3.*// material3 calling
import androidx.compose.material3.Scaffold// Scaffold calling
import androidx.compose.runtime.*// runtime calling
import androidx.compose.ui.Alignment// Alignment calling
import androidx.compose.ui.Modifier// Modifier calling
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType// KeyboardType calling
import androidx.compose.ui.text.input.PasswordVisualTransformation// PasswordVisualTransformation calling
import androidx.compose.ui.unit.dp// dp calling
import androidx.lifecycle.lifecycleScope// lifecycleScope calling
import com.example.mylearn.database.MyDatabaseDataContainer// MyDatabaseDataContainer calling
import com.example.mylearn.ui.theme.MyLearnTheme// MyLearnTheme calling
import kotlinx.coroutines.launch// launch calling
//(Jeff Atwood and Joel Spolsky, 2008)
class MyLoginActivity : ComponentActivity() {
    private lateinit var mydatabaseContainer: MyDatabaseDataContainer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mydatabaseContainer = (application as MyRoomApplication).mydatabaseContainer
        setContent {
            MyLearnTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyLoginScreens(
                        myloginAction = { email, password ->
                            lifecycleScope.launch {
                                // Checking for admin
                                if (email == "admin" && password == "admin") {
                                    (application as MyRoomApplication).myadminLogin(email, password)
                                    runOnUiThread {
                                        Toast.makeText(this@MyLoginActivity, "Welcome Admin!", Toast.LENGTH_SHORT).show()
                                    }
                                    val myintent = Intent(this@MyLoginActivity, AdminDashboardActivity::class.java)//if logged in valid then goto admin-dashboard
                                    startActivity(myintent)
                                    finish()
                                } else {
                                    // Checking for user
                                    val myuser = mydatabaseContainer.MyusersRepositoryInterface.getMyUserByEmailAndPassword(email, password)
                                    if (myuser != null) {
                                        (application as MyRoomApplication).mylogin(myuser)
                                        runOnUiThread {
                                            Toast.makeText(this@MyLoginActivity, "Welcome!", Toast.LENGTH_SHORT).show()
                                        }
                                        val myintent = Intent(this@MyLoginActivity, AllPostActivity::class.java)//if logged in valid then goto student-dashboard
                                        startActivity(myintent)
                                        finish()
                                    } else {
                                        runOnUiThread {
                                            Toast.makeText(this@MyLoginActivity, "Invalid email or password", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            }
                        },
                        myregisterAction = {
                            val myintent = Intent(this, MyregisterActivity::class.java)
                            startActivity(myintent)
                        }
                    )
                }
            }
        }
    }
}
@Composable
fun MyLoginScreens(
    myloginAction: (String, String) -> Unit,
    myregisterAction: () -> Unit
) {
    var newemail by remember { mutableStateOf("") }
    var newpassword by remember { mutableStateOf("") }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        // Adding logo_clz image from drawable
        Image(
            painter = painterResource(id = R.drawable.logo_clz),
            contentDescription = "App Logo",
            modifier = Modifier.size(155.dp),
            contentScale = ContentScale.Crop
        )
        Text(text = "Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(18.5.dp))
        TextField(
            value = newemail,
            onValueChange = { newemail = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(0.8f)
        )
        Spacer(modifier = Modifier.height(10.5.dp))
        TextField(
            value = newpassword,
            onValueChange = { newpassword = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(0.8f)
        )
        Spacer(modifier = Modifier.height(18.5.dp))
        Button(onClick = { myloginAction(newemail, newpassword) }, modifier = Modifier.fillMaxWidth(0.8f)) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(10.5.dp))
        TextButton(onClick = myregisterAction) {
            Text("Register")
        }
    }
}
// Jeff Atwood and Joel Spolsky (2008) stackoverflow. [Online]. Available from:https://stackoverflow.com [Accessed 6 August 2024].