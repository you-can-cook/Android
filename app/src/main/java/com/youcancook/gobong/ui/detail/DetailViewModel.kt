package com.youcancook.gobong.ui.detail

import androidx.lifecycle.viewModelScope
import com.youcancook.gobong.model.Card
import com.youcancook.gobong.model.RecipeStep
import com.youcancook.gobong.model.repository.GoBongRepository
import com.youcancook.gobong.model.repository.UserRepository
import com.youcancook.gobong.ui.base.NetworkViewModel
import com.youcancook.gobong.util.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val goBongRepository: GoBongRepository,
    private val userRepository: UserRepository,
) : NetworkViewModel() {
    private val _isMine = MutableStateFlow(false)
    val isMine: StateFlow<Boolean> get() = _isMine

    private val _isReviewed = MutableStateFlow(false)
    val isReviewed: StateFlow<Boolean> get() = _isReviewed

    private val _isBookmarked = MutableStateFlow(false)
    val isBookmarked: StateFlow<Boolean> get() = _isBookmarked

    private val _isDeleted = MutableStateFlow(false)
    val isDeleted: StateFlow<Boolean> get() = _isDeleted

    private val _cardInfo = MutableStateFlow(Card.createEmpty())
    val cardInfo: StateFlow<Card> get() = _cardInfo

    private val _recipes = MutableStateFlow<List<RecipeStep>>(emptyList())
    val recipes: StateFlow<List<RecipeStep>> get() = _recipes

    private val _activeRecipeStep = MutableStateFlow(0)
    val activeRecipeStep: StateFlow<Int> get() = _activeRecipeStep

    private val _starCount = MutableStateFlow(0)
    val starCount: StateFlow<Int> get() = _starCount

    fun setStar(count: Int) {
        _starCount.value = count
    }

    fun getStar() = _starCount.value

    fun getCurrentRecipe(recipePostId: Int) {
        viewModelScope.launch {
            setNetworkState(NetworkState.LOADING)
            try {
                requestCurrentRecipe(recipePostId)
                setNetworkState(NetworkState.SUCCESS)
            } catch (e: Exception) {
                setNetworkState(NetworkState.FAIL)
                setSnackBarMessage(e.message ?: "")
            }
        }
    }

    fun getTools() = _cardInfo.value.tools

    fun getId() = _cardInfo.value.user.userId

    private suspend fun requestCurrentRecipe(recipePostId: Int) {
        val response = goBongRepository.getCurrentRecipe(recipePostId)
        _cardInfo.value = response.cardInfo.copy(
            description = response.description,
            ingredients = response.ingredients
        )
        _recipes.value = response.recipes
        _isMine.value = response.cardInfo.user.notMine.not()
        _starCount.value = response.myRate
        if (_starCount.value != 0) _isReviewed.value = true
        _isBookmarked.value = _cardInfo.value.bookmarked
    }

    fun activeRecipeStep(position: Int) {
        _activeRecipeStep.value = position
    }

    fun bookmarkRecipe(isBookmarked: Boolean) {
        viewModelScope.launch {
            setNetworkState(NetworkState.LOADING)
            try {
                requestBookmark(isBookmarked)
                setNetworkState(NetworkState.SUCCESS)
            } catch (e: Exception) {
                setNetworkState(NetworkState.FAIL)
                setSnackBarMessage(e.message ?: "")
            }
        }
    }

    private suspend fun requestBookmark(isBookmarked: Boolean) {
        goBongRepository.bookmarkRecipe(isBookmarked, _cardInfo.value.id)
        _cardInfo.value = _cardInfo.value.copy(bookmarked = isBookmarked)
    }

    fun deleteRecipePost() {
        viewModelScope.launch {
            setNetworkState(NetworkState.LOADING)
            try {
                requestDeleteRecipe()
                _isDeleted.value = true
                setNetworkState(NetworkState.SUCCESS)
            } catch (e: Exception) {
                setNetworkState(NetworkState.FAIL)
                setSnackBarMessage(e.message ?: "")
            }
        }
    }

    private suspend fun requestDeleteRecipe() {
        goBongRepository.deleteRecipe(_cardInfo.value.id)
    }

    fun reviewRecipe() {
        viewModelScope.launch {
            setNetworkState(NetworkState.LOADING)
            try {
                if (_isReviewed.value.not()) {
                    requestReview()
                    _isReviewed.value = true
                } else {
                    requestUpdatedReview()
                }
                setNetworkState(NetworkState.SUCCESS)
            } catch (e: Exception) {
                setNetworkState(NetworkState.FAIL)
                if (_cardInfo.value.user.notMine) {
                    setSnackBarMessage(e.message ?: "")
                } else {
                    setSnackBarMessage("내가 올린 게시물에는 리뷰를 남길 수 없습니다.")
                    _starCount.value = 0
                }
            }
        }
    }

    private suspend fun requestReview() {
        goBongRepository.reviewRecipe(_cardInfo.value.id, _starCount.value)
    }

    private suspend fun requestUpdatedReview() {
        goBongRepository.updateReviewRecipe(_cardInfo.value.id, _starCount.value)
    }

    fun follow() {
        viewModelScope.launch {
            setNetworkState(NetworkState.LOADING)
            try {
                requestFollow()
                setNetworkState(NetworkState.SUCCESS)
            } catch (e: Exception) {
                setNetworkState(NetworkState.FAIL)
                setSnackBarMessage(e.message ?: "")
            }
        }
    }

    fun unfollow() {
        viewModelScope.launch {
            setNetworkState(NetworkState.LOADING)
            try {
                requestUnfollow()
                setNetworkState(NetworkState.SUCCESS)
            } catch (e: Exception) {
                setNetworkState(NetworkState.FAIL)
                setSnackBarMessage(e.message ?: "")
            }
        }
    }

    private suspend fun requestFollow() {
        userRepository.follow(_cardInfo.value.user.userId)
        _cardInfo.value = _cardInfo.value.copy(user = _cardInfo.value.user.copy(followed = true))
    }

    private suspend fun requestUnfollow() {
        userRepository.unfollow(_cardInfo.value.user.userId)
        _cardInfo.value = _cardInfo.value.copy(user = _cardInfo.value.user.copy(followed = false))
    }

}