package com.youcancook.gobong

import com.youcancook.gobong.di.GoBongContainer
import com.youcancook.gobong.fake.FakeGoBongContainer
import com.youcancook.gobong.model.RecipeStepAdded
import com.youcancook.gobong.ui.addRecipe.AddRecipeViewModel
import com.youcancook.gobong.util.NetworkState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AddRecipeTest {

    private lateinit var viewModel: AddRecipeViewModel
    private lateinit var fakeViewModel: AddRecipeViewModel
    private val appContainer = GoBongContainer()
    private val fakeContainer = FakeGoBongContainer()

    @Before
    fun setUp() {
        viewModel = AddRecipeViewModel(appContainer.goBongRepository)
        fakeViewModel = AddRecipeViewModel(fakeContainer.goBongRepository)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun isThumbnailEmpty() {
        //Given
        viewModel.setThumbnailByteArray(byteArrayOf())

        //When
        viewModel.uploadNewRecipePost(emptyList())

        //Then
        assertEquals("대표사진을 올려주세요", viewModel.snackBarMessage.value)
    }

    @Test
    fun isIngredientsEmpty() {
        //Given
        viewModel.setThumbnailByteArray(ByteArray(10))
        viewModel.titleInput.value = "테스트"

        //When
        viewModel.uploadNewRecipePost(emptyList())

        //Then
        assertEquals("재료를 입력해주세요", viewModel.snackBarMessage.value)
    }

    @Test
    fun isLevelEmpty() {
        //Given
        viewModel.setThumbnailByteArray(ByteArray(10))
        viewModel.titleInput.value = "테스트"

        //When
        viewModel.uploadNewRecipePost(listOf("레시피 재료"))

        //Then
        assertEquals("난이도를 선택해주세요", viewModel.snackBarMessage.value)
    }

    @Test
    fun isRecipeStepEmpty() {
        //Given
        viewModel.setThumbnailByteArray(ByteArray(10))
        viewModel.titleInput.value = "테스트"
        viewModel.setLevel("쉬워요")

        //When
        viewModel.uploadNewRecipePost(listOf("레시피 재료"))

        //Then
        assertEquals("레시피를 추가해주세요", viewModel.snackBarMessage.value)
    }

    @Test
    fun isRecipeUploadSuccess() {
        //Given
        viewModel.setThumbnailByteArray(ByteArray(10))
        viewModel.titleInput.value = "테스트"
        viewModel.setLevel("쉬워요")
        viewModel.addNewRecipeStep(
            RecipeStepAdded(
                "4분",
                listOf("전자레인지"),
                byteArrayOf(),
                "전자레인지에 4분 돌려주세요",
            )
        )

        //When
        viewModel.uploadNewRecipePost(listOf("레시피 재료"))
        CoroutineScope(Dispatchers.Main).launch {
            delay(5000)
        }

        //Then
        assertEquals(NetworkState.SUCCESS, viewModel.networkState.value)

    }

    @Test
    fun isRecipeUploadFail() {
        //Given
        fakeViewModel.setThumbnailByteArray(ByteArray(10))
        fakeViewModel.titleInput.value = "테스트"
        fakeViewModel.setLevel("쉬워요")
        fakeViewModel.addNewRecipeStep(
            RecipeStepAdded(
                "4분",
                listOf("전자레인지"),
                byteArrayOf(),
                "전자레인지에 4분 돌려주세요",
            )
        )

        //When
        fakeViewModel.uploadNewRecipePost(listOf("레시피 재료"))
        CoroutineScope(Dispatchers.Main).launch {
            delay(5000)
        }

        //Then
        assertEquals(NetworkState.FAIL, fakeViewModel.networkState.value)
        assertEquals("네트워크 오류", fakeViewModel.snackBarMessage.value)

    }

}