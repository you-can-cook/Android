package com.youcancook.gobong.ui.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.edit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.youcancook.gobong.R
import com.youcancook.gobong.databinding.ActivityRegisterBinding
import com.youcancook.gobong.model.LoginUser
import com.youcancook.gobong.ui.ImageActivity
import com.youcancook.gobong.ui.MainActivity
import com.youcancook.gobong.ui.RoutingActivity
import com.youcancook.gobong.ui.base.NetworkActivity
import com.youcancook.gobong.ui.base.NetworkStateListener
import com.youcancook.gobong.util.ACCESS_TOKEN_KEY
import com.youcancook.gobong.util.REFRESH_TOKEN_KEY
import com.youcancook.gobong.util.TOKEN_KEY
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RegisterActivity :
    NetworkActivity<ActivityRegisterBinding, RegisterUserViewModel>(R.layout.activity_register) {

    override val viewModel: RegisterUserViewModel by lazy {
        RegisterUserViewModel(appContainer.userRepository)
    }

    override val onNetworkStateChange: NetworkStateListener = object : NetworkStateListener {
        override fun onSuccess() {
            saveToken()
        }

        override fun onFail() {
        }

        override fun onDone() {
        }

    }
    private var imagePickActivityLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.vm = viewModel

        val auth = intent?.getSerializableExtra(LoginActivity.LOGIN_AUTH) as LoginUser
        viewModel.setLoginUser(auth)

        imagePickActivityLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val imageUri = result.data?.getStringExtra(ImageActivity.IMAGE_DATA_TAG)
                    imageUri ?: return@registerForActivityResult
                    val imageData = ImageActivity.getImageByteArray(this, Uri.parse(imageUri))
                    imageData ?: return@registerForActivityResult
                    Log.e("GOBONG", "imageUri $imageUri $imageData")
                    viewModel.setProfileImageByteArray(imageData)
                }
            }

        binding.run {
            profileImageView.setOnClickListener {
                Intent(this@RegisterActivity, ImageActivity::class.java).apply {
                    putExtra(ImageActivity.PHOTO_SIZE, ImageActivity.PROFILE)
                }.run {
                    imagePickActivityLauncher?.launch(this)
                }
            }

            registerButton.setOnClickListener {
                viewModel.registerNickname()
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorMessage.collectLatest {
                    if (it.isNotEmpty()) {
                        binding.nicknameTextLayout.error = it
                    }
                }
            }
        }
    }

    private fun saveToken() {
        val token = viewModel.getToken()

        getSharedPreferences(TOKEN_KEY, Context.MODE_PRIVATE).edit {
            putString(ACCESS_TOKEN_KEY, token.accessToken)
            putString(REFRESH_TOKEN_KEY, token.refreshToken)
            commit()

            moveToRoutingActivity()
        }
    }

    private fun moveToRoutingActivity() {
        val intent = Intent(this, RoutingActivity::class.java)
            .apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
        startActivity(intent)
    }
}