package com.youcancook.gobong

import com.youcancook.gobong.di.GoBongContainer
import com.youcancook.gobong.fake.FakeGoBongContainer
import com.youcancook.gobong.ui.home.HomeViewModel
import com.youcancook.gobong.util.NetworkState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

class HomeNetworkTest {
    private lateinit var viewModel: HomeViewModel
    private lateinit var fakeViewModel: HomeViewModel
    private val appContainer = GoBongContainer()
    private val fakeContainer = FakeGoBongContainer()

    @Before
    fun setUp() {
        viewModel = HomeViewModel(appContainer.goBongRepository, appContainer.userRepository)
        fakeViewModel = HomeViewModel(fakeContainer.goBongRepository, fakeContainer.userRepository)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun isEmptyList() {
        //Then
        assertEquals(0, viewModel.recipes.value.size)
    }

    @Test
    fun accessTokenExpiredTest() {

    }

    @Test
    fun getRecipeErrorTest() {
        //When
        fakeViewModel.getFollowingRecipes()

        //Then
        assertEquals(NetworkState.FAIL, fakeViewModel.networkState.value)
        assertEquals("네트워크 오류", fakeViewModel.snackBarMessage.value)
    }

    @Test
    fun followCorrectTest() {
        //When
        viewModel.follow("")

        //Then
        assertEquals(NetworkState.SUCCESS, viewModel.networkState.value)
    }

    @Test
    fun followErrorTest() {
        //When
        fakeViewModel.follow("")

        //Then
        assertEquals(NetworkState.FAIL, fakeViewModel.networkState.value)
        assertEquals("이미 팔로우한 사용자입니다.", fakeViewModel.snackBarMessage.value)
    }

    @Test
    fun unfollowCorrectTest() {
        //When
        viewModel.unfollow("")

        //Then
        assertEquals(NetworkState.SUCCESS, viewModel.networkState.value)
    }

    @Test
    fun unfollowErrorTest() {
        //When
        fakeViewModel.unfollow("")

        //Then
        assertEquals(NetworkState.FAIL, fakeViewModel.networkState.value)
        assertEquals("팔로우하지 않은 사용자입니다.", fakeViewModel.snackBarMessage.value)
    }

    @Test
    fun refreshTest() {
        //When
        viewModel.getFollowingRecipes()

        //Then
        assertNotEquals(NetworkState.LOADING, viewModel.networkState.value)
    }
}