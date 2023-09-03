package com.youcancook.gobong

import com.youcancook.gobong.di.GoBongContainer
import com.youcancook.gobong.fake.FakeGoBongContainer
import com.youcancook.gobong.ui.my.OthersProfileViewModel
import com.youcancook.gobong.util.NetworkState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class OthersProfileTest {

    private lateinit var viewModel: OthersProfileViewModel
    private lateinit var fakeViewModel: OthersProfileViewModel
    private val appContainer = GoBongContainer()
    private val fakeContainer = FakeGoBongContainer()

    @Before
    fun setUp() {
        viewModel =
            OthersProfileViewModel(appContainer.goBongRepository, appContainer.userRepository)
        fakeViewModel =
            OthersProfileViewModel(fakeContainer.goBongRepository, fakeContainer.userRepository)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun getOthersDataSuccess() {
        //When
        viewModel.getOthersInfo("userId")
        CoroutineScope(Dispatchers.Main).launch {
            delay(5000)
        }

        //Then
        assertEquals(NetworkState.SUCCESS, viewModel.networkState.value)
    }

    @Test
    fun getMyDataError() {
        //When
        fakeViewModel.getOthersInfo("userId")
        CoroutineScope(Dispatchers.Main).launch {
            delay(5000)
        }

        //Then
        assertEquals(NetworkState.FAIL, fakeViewModel.networkState.value)
        assertEquals("네트워크 오류", fakeViewModel.snackBarMessage.value)
    }

    @Test
    fun followUserSuccess() {
        //Given
        viewModel.unfollow()
        CoroutineScope(Dispatchers.Main).launch {
            delay(5000)
        }

        //When
        viewModel.follow()
        CoroutineScope(Dispatchers.Main).launch {
            delay(5000)
        }

        //Then
        assertEquals(NetworkState.SUCCESS, viewModel.networkState.value)
        assertTrue(viewModel.user.value.followed)
    }

    @Test
    fun followUserError() {
        //When
        fakeViewModel.follow()
        CoroutineScope(Dispatchers.Main).launch {
            delay(5000)
        }

        //Then
        assertEquals(NetworkState.FAIL, fakeViewModel.networkState.value)
        assertEquals("이미 팔로우한 사용자입니다.", fakeViewModel.snackBarMessage.value)
    }

    @Test
    fun unfollowUserSuccess() {
        //Given
        viewModel.follow()
        CoroutineScope(Dispatchers.Main).launch {
            delay(5000)
        }

        //When
        viewModel.unfollow()
        CoroutineScope(Dispatchers.Main).launch {
            delay(5000)
        }

        //Then
        assertEquals(NetworkState.SUCCESS, viewModel.networkState.value)
        assertFalse(viewModel.user.value.followed)
    }

    @Test
    fun unfollowUserError() {
        //When
        fakeViewModel.unfollow()
        CoroutineScope(Dispatchers.Main).launch {
            delay(5000)
        }

        //Then
        assertEquals(NetworkState.FAIL, fakeViewModel.networkState.value)
        assertEquals("팔로우하지 않은 사용자입니다.", fakeViewModel.snackBarMessage.value)
    }
}