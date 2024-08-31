package com.example.mylearn.dao // dao package calling
import androidx.room.*// all room package calling
import com.example.mylearn.entity.Myuser//Myuser calling
import kotlinx.coroutines.flow.Flow//Flow calling
//(Stevdza-San, 2020)
@Dao
interface MyuserDao {
    // for insert item
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(myuser: Myuser)
    // for update item
    @Update
    suspend fun update(myuser: Myuser)
    // for delete item
    @Delete
    suspend fun delete(myuser: Myuser)
    // fetching with userid
    @Query("SELECT * FROM myuser_table WHERE userid = :userId")
    fun getMyuser(userId: Int): Flow<Myuser?> // Changed from suspend fun to fun
    // fetching all users
    @Query("SELECT * FROM myuser_table ORDER BY u_name ASC")
    fun getAllMyusers(): Flow<List<Myuser>>
    // fetching email and password
    @Query("SELECT * FROM myuser_table WHERE u_email = :email AND u_password = :password LIMIT 1")
    fun getMyUserByEmailAndPassword(email: String, password: String): Myuser?
}
//Stevdza-San (2020) ROOM Database - #2 Insert Data | Android Studio Tutorial. [Online]. Available from: https://www.youtube.com/watch?v=UBCAWfztTrQ&list=PLSrm9z4zp4mEPOfZNV9O-crOhoMa0G2-o&index=3 [Accessed 10 August 2024].
//Stevdza-San (2020) ROOM Database - #4 Update Data | Android Studio Tutorial. [Online]. Available from: https://www.youtube.com/watch?v=5rfBU75sguk&list=PLSrm9z4zp4mEPOfZNV9O-crOhoMa0G2-o&index=4 [Accessed 11 August 2024].