package com.example.medplus_user.domain.useCases

import com.example.medplus_user.domain.models.Category
import com.example.medplus_user.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repo: UserRepository
) {
    operator fun invoke(): Flow<List<Category>> = repo.getCategories()
}
