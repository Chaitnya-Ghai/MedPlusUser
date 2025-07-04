package com.example.medplus_user.domain.useCases

import com.example.medplus_user.common.Resource
import com.example.medplus_user.domain.models.Medicines
import com.example.medplus_user.domain.repository.UserRepository
import javax.inject.Inject



class GetAllMedicinesUseCase @Inject constructor(
    private val repo: UserRepository,
) {
    suspend operator fun invoke(): Resource<List<Medicines>> = repo.getAllMedicines()
}
class GetaMedicineByIdUseCase @Inject constructor(
    private val repo: UserRepository,
) {
//    suspend operator fun invoke(medId: String): List<Medicines> = repo.getMedicine(medId)
}
class GetMedicinesByCategoryUseCase @Inject constructor(
    private val repo: UserRepository,
) {
    suspend operator fun invoke(catId: String): Resource<List<Medicines>> = repo.getMedicinesByCategory(catId)
}
class GetMedicineByNameUseCase @Inject constructor(
    private val repo: UserRepository,
) {
//    suspend operator fun invoke(name: String): List<Medicines> = repo.getMedicinesByName(name = name)
}
