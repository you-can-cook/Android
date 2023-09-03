package com.youcancook.gobong.ui.my.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.youcancook.gobong.R
import com.youcancook.gobong.databinding.FragmentSettingBinding
import com.youcancook.gobong.util.ACCESS_TOKEN_KEY
import com.youcancook.gobong.util.REFRESH_TOKEN_KEY
import com.youcancook.gobong.util.TOKEN_KEY

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private val logoutAlertDialog: AlertDialog by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("로그아웃하시겠습니까?")
            .setPositiveButton("네") { _, _ ->
                removeToken()
            }
            .setNegativeButton("취소") { _, _ ->

            }.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            backArrowButton.setOnClickListener {
                requireActivity().finish()
            }

            myAccountMoreImageView.setOnClickListener {
                findNavController().navigate(R.id.action_settingFragment_to_settingProfileFragment)
            }

            alarmMoreImageView.setOnClickListener {
                findNavController().navigate(R.id.action_settingFragment_to_settingNotificationFragment)
            }

            logoutMoreImageView.setOnClickListener {
                logoutAlertDialog.show()
            }

            quitMoreImageView.setOnClickListener {
                findNavController().navigate(R.id.action_settingFragment_to_settingQuitFragment)
            }
        }
    }

    private fun removeToken() {
        val token = requireContext().getSharedPreferences(TOKEN_KEY, Context.MODE_PRIVATE)
        token.edit().remove(ACCESS_TOKEN_KEY).commit()
        token.edit().remove(REFRESH_TOKEN_KEY).commit()

        val intent = requireActivity().packageManager.getLaunchIntentForPackage(
            requireActivity().packageName
        )
        val componentName = intent!!.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        startActivity(mainIntent)
        System.exit(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}