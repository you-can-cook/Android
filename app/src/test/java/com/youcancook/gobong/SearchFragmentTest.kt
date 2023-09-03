package com.youcancook.gobong

import com.youcancook.gobong.di.GoBongContainer
import com.youcancook.gobong.fake.FakeGoBongContainer
import com.youcancook.gobong.model.Filter
import com.youcancook.gobong.ui.search.SearchViewModel
import com.youcancook.gobong.util.NetworkState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchFragmentTest {

    private lateinit var viewModel: SearchViewModel
    private lateinit var fakeViewModel: SearchViewModel
    private val appContainer = GoBongContainer()
    private val fakeContainer = FakeGoBongContainer()

    @Before
    fun setUp() {
        viewModel = SearchViewModel(appContainer.goBongRepository, appContainer.userRepository)
        fakeViewModel =
            SearchViewModel(fakeContainer.goBongRepository, fakeContainer.userRepository)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun getAllRecipesWithoutFilterSuccess() {
        //Given
        viewModel.setFilter(Filter.createEmpty())

        //When
        viewModel.getAllRecipes()
        CoroutineScope(Dispatchers.Main).launch {
            delay(5000)
        }

        //Then
        assertEquals(NetworkState.SUCCESS, viewModel.networkState.value)

    }

    @Test
    fun getAllRecipesWithoutFilterError() {
        //Given
        fakeViewModel.setFilter(Filter.createEmpty())

        //When
        fakeViewModel.getAllRecipes()
        CoroutineScope(Dispatchers.Main).launch {
            delay(5000)
        }

        //Then
        assertEquals(NetworkState.FAIL, fakeViewModel.networkState.value)
        assertEquals("네트워크 오류", fakeViewModel.snackBarMessage.value)

    }

    //TODO 네트워크 연결 후 통과되는지 확인
    @Test
    fun getAllRecipesWithFilterSuccess() {
        //Given
        viewModel.setFilter(
            Filter(
                "기",
                "최신순",
                "쉬워요",
                "4",
                "2",
                listOf(
                    "전자레인지"
                )
            )
        )

        //When
        viewModel.filter("기")
        CoroutineScope(Dispatchers.Main).launch {
            delay(5000)
        }

        //Then
        assertEquals(NetworkState.SUCCESS, viewModel.networkState.value)
        assertEquals(viewModel.recipes.value.filter {
            it.title.contains("기") &&
                    it.level == "쉬워요" &&
                    it.time.split(" ")[0].removeSuffix("분") >= "4" &&
                    it.star >= "2" &&
                    it.tools.contains("전자레인지")
        }, viewModel.recipes.value)

    }


    @Test
    fun getAllRecipesWithFilterError() {
        //Given
        fakeViewModel.setFilter(
            Filter(
                "기",
                "최신순",
                "쉬워요",
                "4",
                "2",
                listOf(
                    "전자레인지"
                )
            )
        )

        //When
        fakeViewModel.filter("기")
        CoroutineScope(Dispatchers.Main).launch {
            delay(5000)
        }

        //Then
        assertEquals(NetworkState.FAIL, fakeViewModel.networkState.value)
        assertEquals("네트워크 오류", fakeViewModel.snackBarMessage.value)

    }

}