package com.example.mylearn// mylearn package calling
import android.app.Application// Application calling
import com.example.mylearn.database.MyDatabaseDataContainer// MyMyDatabaseDataContainer calling
import com.example.mylearn.entity.Myuser// Myuser calling
//(Jeff Atwood and Joel Spolsky, 2008)
class MyRoomApplication : Application() {
    lateinit var mydatabaseContainer: MyDatabaseDataContainer// MyMyDatabaseDataContainer calling
    var myloggedInUser: Myuser? = null// Myuser calling
    var ismyAdminLoggedIn: Boolean = false// ismyAdminLoggedIn calling
    var myadminEmail: String? = null// myadminEmail calling
    var myadminPassword: String? = null// myadminPassword calling
    override fun onCreate() {
        super.onCreate()// super calling
        mydatabaseContainer = MyDatabaseDataContainer(this)// MyMyDatabaseDataContainer calling with this
    }
    // logout function
    fun do_logout() {
        myloggedInUser = null
        ismyAdminLoggedIn = false
        myadminEmail = null
        myadminPassword = null
    }
    // normal users login function
    fun mylogin(myuser: Myuser) {
        myloggedInUser = myuser
        ismyAdminLoggedIn = false
    }
    // admin login function
    fun myadminLogin(email: String, password: String) {
        myadminEmail = email
        myadminPassword = password
        ismyAdminLoggedIn = true
    }
}
// Jeff Atwood and Joel Spolsky (2008) stackoverflow. [Online]. Available from:https://stackoverflow.com [Accessed 2 August 2024].