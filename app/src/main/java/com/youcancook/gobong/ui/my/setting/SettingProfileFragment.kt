package com.youcancook.gobong.ui.my.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.youcancook.gobong.R
import com.youcancook.gobong.adapter.bindingAdapter.setProfileImageUrl
import com.youcancook.gobong.databinding.FragmentSettingProfileBinding
import com.youcancook.gobong.ui.ImageActivity
import com.youcancook.gobong.ui.base.NetworkFragment
import com.youcancook.gobong.ui.base.NetworkStateListener
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SettingProfileFragment :
    NetworkFragment<FragmentSettingProfileBinding, SettingUserViewModel>(R.layout.fragment_setting_profile) {

    override val viewModel: SettingUserViewModel by lazy {
        SettingUserViewModel(appContainer.goBongRepository, appContainer.userRepository)
    }
    override val onNetworkStateChange: NetworkStateListener = object : NetworkStateListener {
        override fun onSuccess() {
            findNavController().popBackStack()
        }

        override fun onFail() {
        }

        override fun onDone() {
        }

    }
    private var imagePickActivityLauncher: ActivityResultLauncher<Intent>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.vm = viewModel

        super.onViewCreated(view, savedInstanceState)

        imagePickActivityLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == AppCompatActivity.RESULT_OK) {
                    val imageUri = result.data?.getStringExtra(ImageActivity.IMAGE_DATA_TAG)
                    imageUri ?: return@registerForActivityResult
                    val imageData =
                        ImageActivity.getImageByteArray(requireContext(), Uri.parse(imageUri))
                    imageData ?: return@registerForActivityResult
                    Log.e("GOBONG", "imageUri $imageUri $imageData")
                    viewModel.setProfileImageByteArray(imageData)
                }
            }

        binding.run {
            backArrowButton.setOnClickListener {
                findNavController().popBackStack()
            }

            profileImageView.setOnClickListener {
                Intent(requireContext(), ImageActivity::class.java).apply {
                    putExtra(ImageActivity.PHOTO_SIZE, ImageActivity.PROFILE)
                }.run {
                    imagePickActivityLauncher?.launch(this)
                }
            }

            completeButton.setOnClickListener {
                viewModel.updateUserProfile()
            }

        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.profileImage.collectLatest {
                    if (it.isNotEmpty()) {
                       binding.profileImageView.setProfileImageUrl(it)
                    }
                }
            }
        }

        viewModel.getOldProfile()
    }
}