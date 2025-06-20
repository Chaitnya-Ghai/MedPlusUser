package com.example.medplus_user.domain.useCases

import com.example.medplus_user.domain.models.Shopkeeper
import com.example.medplus_user.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShopkeeperUseCases @Inject constructor(
    private val repo: UserRepository
){
    operator fun invoke(id: String): Flow<List<Shopkeeper>> = repo.getShopkeepers(medicineId = id)
}

