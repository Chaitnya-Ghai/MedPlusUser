package com.example.medplus_user.domain.useCases

import com.example.medplus_user.common.Resource
import com.example.medplus_user.domain.models.Shopkeeper
import com.example.medplus_user.domain.repository.UserRepository
import javax.inject.Inject

class ShopkeeperUseCases @Inject constructor(
    private val repo: UserRepository
){
    suspend operator fun invoke(): Resource<List<Shopkeeper>> = repo.getAllShopkeepers()
}

