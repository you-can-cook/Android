package com.youcancook.gobong

import com.youcancook.gobong.di.GoBongContainer
import com.youcancook.gobong.fake.FakeGoBongContainer
import com.youcancook.gobong.ui.my.MyProfileViewModel
import com.youcancook.gobong.util.NetworkState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MyProfileTest {

    private lateinit var viewModel: MyProfileViewModel
    private lateinit var fakeViewModel: MyProfileViewModel
    private val appContainer = GoBongContainer()
    private val fakeContainer = FakeGoBongContainer()

    @Before
    fun setUp() {
        viewModel = MyProfileViewModel(appContainer.goBongRepository)
        fakeViewModel =
            MyProfileViewModel(fakeContainer.goBongRepository)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun getMyDataSuccess() {
        //When
        viewModel.getMyInfo()
        CoroutineScope(Dispatchers.Main).launch {
            delay(5000)
        }

        //Then
        assertEquals(NetworkState.SUCCESS, viewModel.networkState.value)
    }

    @Test
    fun getMyDataError() {
        //When
        fakeViewModel.getMyInfo()
        CoroutineScope(Dispatchers.Main).launch {
            delay(5000)
        }

        //Then
        assertEquals(NetworkState.FAIL, fakeViewModel.networkState.value)
        assertEquals("네트워크 오류", fakeViewModel.snackBarMessage.value)
    }
}