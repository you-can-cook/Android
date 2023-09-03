package com.youcancook.gobong.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.edit
import com.youcancook.gobong.R
import com.youcancook.gobong.databinding.ActivityRoutingBinding
import com.youcancook.gobong.ui.base.NetworkActivity
import com.youcancook.gobong.ui.base.NetworkStateListener
import com.youcancook.gobong.ui.login.LoginActivity
import com.youcancook.gobong.util.ACCESS_TOKEN
import com.youcancook.gobong.util.ACCESS_TOKEN_KEY
import com.youcancook.gobong.util.REFRESH_TOKEN
import com.youcancook.gobong.util.REFRESH_TOKEN_KEY
import com.youcancook.gobong.util.TOKEN_KEY

class RoutingActivity :
    NetworkActivity<ActivityRoutingBinding, RoutingViewModel>(R.layout.activity_routing) {

    override val viewModel: RoutingViewModel by lazy {
        RoutingViewModel(appContainer.userRepository)
    }

    override val onNetworkStateChange: NetworkStateListener = object : NetworkStateListener {
        override fun onSuccess() {
            //accessToken 토큰 새로 발급 성공
            saveToken()
            val intent = Intent(this@RoutingActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        override fun onFail() {
            //REFRESH TOKEN 만료
            val intent = Intent(this@RoutingActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        override fun onDone() {
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isTokenExist().not()) {
            println("token Not Exist")
            val intent = Intent(this@RoutingActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun isTokenExist(): Boolean {
        println("isTokenExist")
        val token = getSharedPreferences(
            TOKEN_KEY,
            Context.MODE_PRIVATE
        )

        val accessToken = token.getString(ACCESS_TOKEN_KEY, "")

        if (accessToken.isNullOrEmpty()) return false

        println("oldToken $accessToken")
        viewModel.getAccessToken(
            token.getString(REFRESH_TOKEN_KEY, "") ?: ""
        )

        return true
    }

    private fun saveToken() {
        val token = viewModel.getToken()
        getSharedPreferences(
            TOKEN_KEY,
            Context.MODE_PRIVATE
        ).edit {
            putString(ACCESS_TOKEN_KEY, token.accessToken)
            putString(REFRESH_TOKEN_KEY, token.refreshToken)
            ACCESS_TOKEN = token.accessToken
            REFRESH_TOKEN = token.refreshToken
        }
    }
}
