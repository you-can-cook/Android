package com.youcancook.gobong.ui.my

import com.youcancook.gobong.model.Card
import com.youcancook.gobong.model.User
import com.youcancook.gobong.model.repository.GoBongRepository
import com.youcancook.gobong.ui.base.NetworkViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class ProfileViewModel(
    private val goBongRepository: GoBongRepository,
) : NetworkViewModel() {

    private val _user = MutableStateFlow(User())
    val user: StateFlow<User> = _user

    private val _recipes = MutableStateFlow<List<Card>>(emptyList())
    val recipes: StateFlow<List<Card>> get() = _recipes

    fun setUserInfo(user: User) {
        _user.value = user
    }

    fun getUserInfo() = _user.value

    fun setUserRecipe(recipes: List<Card>) {
        _recipes.value = recipes
    }

}