package com.example.vibramobile.data.repository

import com.example.vibramobile.data.mapper.toDomain
import com.example.vibramobile.data.mapper.toEntity
import com.example.vibramobile.data.source.local.dao.UserDao
import com.example.vibramobile.domain.contract.ILocalUserRepository
import com.example.vibramobile.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class LocalUserRepository(
    private val userDao: UserDao
) : ILocalUserRepository {
    override fun observeUser(): Flow<User?> {
        return userDao.getUser().map { it?.toDomain() }
    }

    override suspend fun getUser(): User? {
        return withContext(Dispatchers.IO) {
            userDao.getUserOnce()?.toDomain()
        }
    }

    override suspend fun getAccessToken(): String {
        return withContext(Dispatchers.IO) {
            userDao.getUserOnce()?.token.orEmpty()
        }
    }

    override suspend fun upsertUser(user: User) {
        withContext(Dispatchers.IO) {
            userDao.insert(user.toEntity())
        }
    }

    override suspend fun clearUser() {
        withContext(Dispatchers.IO) {
            userDao.clear()
        }
    }
}
