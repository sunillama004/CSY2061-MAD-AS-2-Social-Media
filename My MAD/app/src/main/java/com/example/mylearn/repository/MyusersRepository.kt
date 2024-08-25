package com.example.mylearn.repository // repository package calling
import com.example.mylearn.dao.MyuserDao // MyuserDao bringing
import com.example.mylearn.entity.Myuser // Myuser bringing
import kotlinx.coroutines.flow.Flow // Flow bringing
//(Stevdza-San, 2020)
class MyusersRepository(private val myuserDao: MyuserDao) : MyusersRepositoryInterface {
    // fetching email and password
    override suspend fun getMyUserByEmailAndPassword(email: String, password: String): Myuser? {
        return myuserDao.getMyUserByEmailAndPassword(email, password)
    }
    // fetching all users
    override fun getAllMyusersStream(): Flow<List<Myuser>> = myuserDao.getAllMyusers()
    // fetching with userid
    override fun getMyuserStream(userid: Int): Flow<Myuser?> = myuserDao.getMyuser(userid)
    // for insert item
    override suspend fun insertMyuser(myuser: Myuser) = myuserDao.insert(myuser)
    // for delete item
    override suspend fun deleteMyuser(myuser: Myuser) = myuserDao.delete(myuser)
    // for update item
    override suspend fun updateMyuser(myuser: Myuser) = myuserDao.update(myuser)
}
//Stevdza-San (2020) ROOM Database - #2 Insert Data | Android Studio Tutorial. [Online]. Available from: https://www.youtube.com/watch?v=UBCAWfztTrQ&list=PLSrm9z4zp4mEPOfZNV9O-crOhoMa0G2-o&index=3 [Accessed 10 August 2024].
//Stevdza-San (2020) ROOM Database - #4 Update Data | Android Studio Tutorial. [Online]. Available from: https://www.youtube.com/watch?v=5rfBU75sguk&list=PLSrm9z4zp4mEPOfZNV9O-crOhoMa0G2-o&index=4 [Accessed 11 August 2024].