package com.youcancook.gobong

import com.youcancook.gobong.di.GoBongContainer
import com.youcancook.gobong.fake.FakeGoBongContainer
import com.youcancook.gobong.ui.detail.DetailViewModel
import com.youcancook.gobong.util.NetworkState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DetailActivityTest {
    private lateinit var viewModel: DetailViewModel
    private lateinit var fakeViewModel: DetailViewModel
    private val appContainer = GoBongContainer()
    private val fakeContainer = FakeGoBongContainer()

    @Before
    fun setUp() {
        viewModel = DetailViewModel(appContainer.goBongRepository, appContainer.userRepository)
        fakeViewModel =
            DetailViewModel(fakeContainer.goBongRepository, fakeContainer.userRepository)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun getRecipeInfoSuccess() {
        //When
        viewModel.getCurrentRecipe(3)
        CoroutineScope(Dispatchers.Main).launch {
            delay(5000)

            //Then
            assertEquals(NetworkState.SUCCESS, viewModel.networkState.value)
            assertTrue(viewModel.cardInfo.value.title.isNotEmpty())
        }


    }

    @Test
    fun getRecipeInfoError() {
        //When
        fakeViewModel.getCurrentRecipe(3)
        CoroutineScope(Dispatchers.Main).launch {
            delay(5000)
        }

        //Then
        assertEquals(NetworkState.FAIL, fakeViewModel.networkState.value)
        assertEquals("네트워크 오류", fakeViewModel.snackBarMessage.value)
    }

    @Test
    fun bookmarkSuccess() {
        //Given
        viewModel.bookmarkRecipe(false)

        //When
        viewModel.bookmarkRecipe(true)

        //Then
        assertEquals(NetworkState.SUCCESS, viewModel.networkState.value)
        assertTrue(viewModel.cardInfo.value.bookmarked)
    }

    @Test
    fun bookmarkError() {
        //Given
        fakeViewModel.bookmarkRecipe(false)

        //When
        fakeViewModel.bookmarkRecipe(true)

        //Then
        assertEquals(NetworkState.FAIL, fakeViewModel.networkState.value)
        assertEquals("네트워크 오류", fakeViewModel.snackBarMessage.value)
    }

    @Test
    fun unBookmarkSuccess() {
        //Given
        viewModel.bookmarkRecipe(true)

        //When
        viewModel.bookmarkRecipe(false)

        //Then
        assertEquals(NetworkState.SUCCESS, viewModel.networkState.value)
        assertFalse(viewModel.cardInfo.value.bookmarked)
    }

    @Test
    fun unBookmarkError() {
        //Given
        fakeViewModel.bookmarkRecipe(true)

        //When
        fakeViewModel.bookmarkRecipe(false)


        //Then
        assertEquals(NetworkState.FAIL, fakeViewModel.networkState.value)
        assertEquals("네트워크 오류", fakeViewModel.snackBarMessage.value)
    }

    @Test
    fun deleteSuccess() {
        //When
        viewModel.deleteRecipePost()

        //Then
        assertEquals(NetworkState.SUCCESS, viewModel.networkState.value)
        assertTrue(viewModel.isDeleted.value)
    }

    @Test
    fun deleteError() {
        //When
        fakeViewModel.deleteRecipePost()

        //Then
        assertEquals(NetworkState.FAIL, fakeViewModel.networkState.value)
        assertEquals("네트워크 오류", fakeViewModel.snackBarMessage.value)
    }

    @Test
    fun reviewSuccess() {
        //When
        viewModel.setStar(2)
        viewModel.reviewRecipe()

        CoroutineScope(Dispatchers.Main).launch {
            delay(5000)

            //Then
            assertEquals(NetworkState.SUCCESS, viewModel.networkState.value)
            assertNotEquals(viewModel.cardInfo.value.star, "0")
        }

    }

    @Test
    fun reviewError() {
        //When
        fakeViewModel.setStar(2)
        fakeViewModel.reviewRecipe()

        //Then
        assertEquals(NetworkState.FAIL, fakeViewModel.networkState.value)
        assertEquals("네트워크 오류", fakeViewModel.snackBarMessage.value)
    }

    @Test
    fun reviewEditSuccess() {
        //Given
        viewModel.setStar(2)
        viewModel.reviewRecipe()

        //When
        viewModel.setStar(3)
        viewModel.reviewRecipe()


        CoroutineScope(Dispatchers.Main).launch {
            delay(5000)

            //Then
            assertEquals(NetworkState.SUCCESS, viewModel.networkState.value)
            assertNotEquals(viewModel.cardInfo.value.star, "0")
        }

    }

    @Test
    fun reviewEditError() {
        //Given
        fakeViewModel.setStar(2)
        fakeViewModel.reviewRecipe()

        //When
        fakeViewModel.setStar(3)
        fakeViewModel.reviewRecipe()

        //Then
        assertEquals(NetworkState.FAIL, fakeViewModel.networkState.value)
        assertEquals("네트워크 오류", fakeViewModel.snackBarMessage.value)
    }
}