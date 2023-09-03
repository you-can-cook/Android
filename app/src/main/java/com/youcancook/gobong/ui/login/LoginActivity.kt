package com.youcancook.gobong.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.edit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.common.api.Status
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.youcancook.gobong.R
import com.youcancook.gobong.databinding.ActivityLoginBinding
import com.youcancook.gobong.model.LoginUser
import com.youcancook.gobong.ui.MainActivity
import com.youcancook.gobong.ui.base.NetworkActivity
import com.youcancook.gobong.ui.base.NetworkStateListener
import com.youcancook.gobong.util.ACCESS_TOKEN
import com.youcancook.gobong.util.ACCESS_TOKEN_KEY
import com.youcancook.gobong.util.GOOGLE_CLIENT_ID
import com.youcancook.gobong.util.REFRESH_TOKEN
import com.youcancook.gobong.util.REFRESH_TOKEN_KEY
import com.youcancook.gobong.util.TOKEN_KEY
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginActivity :
    NetworkActivity<ActivityLoginBinding, RegisterUserViewModel>(R.layout.activity_login) {
    override val viewModel: RegisterUserViewModel by lazy {
        RegisterUserViewModel(appContainer.userRepository)
    }
    override val onNetworkStateChange: NetworkStateListener = object : NetworkStateListener {
        override fun onSuccess() {
            saveToken()
        }

        override fun onFail() {
            moveToRegister()
        }

        override fun onDone() {
        }

    }

    private var provider: String? = null
    private var userId: String? = null
    private val googleSignInClient: GoogleSignInClient by lazy { getGoogleClient() }
    private val googleAuthLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

            try {
                val account = task.getResult(ApiException::class.java)

                account.id?.let {
                    successGoogleLogin(it)
                } ?: throw ApiException(Status.RESULT_CANCELED)


            } catch (e: ApiException) {
                viewModel.setSnackBarMessage("구글로 로그인 실패")
                Log.e("LOGIN", e.stackTraceToString())
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding.vm = viewModel

        viewModel.makeTemporaryToken()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.temporaryToken.collectLatest {
                    initListeners()
                }
            }
        }
    }

    private fun initListeners() {
        binding.run {

            googleLoginButton.setOnClickListener {
                loginWithGoogle()
            }

            kakaoLoginButton.setOnClickListener {
                loginWithKakao()
            }
        }
    }

    private fun loginWithGoogle() {
        googleSignInClient.signOut()
        val signInIntent = googleSignInClient.signInIntent
        googleAuthLauncher.launch(signInIntent)
    }

    private fun successGoogleLogin(id: String) {
        provider = GOOGLE
        userId = id
        viewModel.setLoginUser(makeLoginUser())
        viewModel.login()
    }

    private fun loginWithKakao() {
        viewModel.loading()
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("LOGIN", "카카오계정으로 로그인 실패", error)
                viewModel.setSnackBarMessage("카카오계정으로 로그인 실패")
            } else if (token != null) {
                Log.i("LOGIN", "카카오계정으로 로그인 성공 ${token.accessToken}")
                successKakaoLogin()
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@LoginActivity)) {
            UserApiClient.instance.loginWithKakaoTalk(this@LoginActivity) { token, error ->
                if (error != null) {
                    Log.e("LOGIN", "카카오톡으로 로그인 실패", error)
                    viewModel.setSnackBarMessage("카카오톡으로 로그인 실패")
                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(
                        this@LoginActivity,
                        callback = callback
                    )
                } else if (token != null) {
                    Log.i("LOGIN", "카카오톡으로 로그인 성공 ${token.accessToken}")
                    successKakaoLogin()

                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(
                this@LoginActivity,
                callback = callback
            )
        }
    }

    private fun successKakaoLogin() {
        provider = KAKAO
        getKakaoLoginUser()
    }

    private fun getKakaoLoginUser() {
        UserApiClient.instance.me { user, error ->
            if (error != null || user == null) {
                return@me
            } else {
                userId = user.id.toString()
                viewModel.setLoginUser(makeLoginUser())
                viewModel.login()
            }
        }
    }

    private fun makeLoginUser() = LoginUser(
        provider ?: "",
        userId.toString(),
        viewModel.getTemporaryToken()
    )

    private fun saveToken() {
        val sharedPref = getSharedPreferences(TOKEN_KEY, Context.MODE_PRIVATE) ?: return
        val token = viewModel.getToken()
        sharedPref.edit {
            putString(ACCESS_TOKEN_KEY, token.accessToken)
            putString(REFRESH_TOKEN_KEY, token.refreshToken)
            commit()

            ACCESS_TOKEN = token.accessToken
            REFRESH_TOKEN = token.refreshToken
            moveToMain()
        }

    }

    private fun moveToMain() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun moveToRegister() {
        val auth = makeLoginUser()
        val intent =
            Intent(this@LoginActivity, RegisterActivity::class.java)
                .apply {
                    putExtra(LOGIN_AUTH, auth)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
        startActivity(intent)
        finish()
    }

    private fun getGoogleClient(): GoogleSignInClient {
        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope("https://www.googleapis.com/auth/pubsub"))
            .requestServerAuthCode(GOOGLE_CLIENT_ID)
            .requestEmail() // 이메일도 요청할 수 있다.
            .build()

        return GoogleSignIn.getClient(this, googleSignInOption)
    }

    companion object {
        const val LOGIN_AUTH = "loginAuth"
        const val KAKAO = "KAKAO"
        const val GOOGLE = "GOOGLE"
    }
}