package com.youcancook.gobong.ui.base

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.youcancook.gobong.databinding.DialogLoadingBinding
import com.youcancook.gobong.util.NetworkState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

interface NetworkStateListener {
    fun onSuccess()
    fun onFail()
    fun onDone()
}

abstract class NetworkFragment<T : ViewDataBinding, VM : NetworkViewModel>(
    @LayoutRes private val layoutRes: Int,
) : GoBongFragment<T>(layoutRes) {

    abstract val viewModel: VM

    abstract val onNetworkStateChange: NetworkStateListener

    private val loadingDialog: AlertDialog by lazy {
        val dialogView = DialogLoadingBinding.inflate(requireActivity().layoutInflater)
        AlertDialog.Builder(requireContext())
            .setView(dialogView.root)
            .setCancelable(false)
            .create().apply {
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.snackBarMessage.collectLatest {
                    if (it.isNotEmpty()) {
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.toastMessage.collectLatest {
                    if (it.isNotEmpty()) {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.networkState.collectLatest {
                    when (it) {
                        NetworkState.DONE -> {
                            loadingDialog.dismiss()
                            onNetworkStateChange.onDone()
                        }

                        NetworkState.LOADING -> {
                            loadingDialog.show()
                        }

                        NetworkState.SUCCESS -> {
                            loadingDialog.dismiss()
                            onNetworkStateChange.onSuccess()
                        }

                        NetworkState.FAIL -> {
                            loadingDialog.dismiss()
                            onNetworkStateChange.onFail()
                        }

                        NetworkState.TOKEN_EXPIRED -> {
                            Toast.makeText(
                                requireContext(),
                                "시간이 지나 앱을 재실행합니다",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = requireActivity().packageManager.getLaunchIntentForPackage(
                                requireActivity().packageName
                            )
                            val componentName = intent!!.component
                            val mainIntent = Intent.makeRestartActivityTask(componentName)
                            startActivity(mainIntent)
                            System.exit(0)
                        }
                    }
                }
            }
        }
    }

}