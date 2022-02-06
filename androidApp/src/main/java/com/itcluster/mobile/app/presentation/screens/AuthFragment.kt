package com.itcluster.mobile.app.presentation.screens

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.core.view.*
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.itcluster.mobile.app.BR
import com.itcluster.mobile.app.R
import com.itcluster.mobile.app.databinding.FragmentAuthBinding
import com.itcluster.mobile.app.ext.hideKeyboard
import com.itcluster.mobile.app.ext.log.LogSniffer
import com.itcluster.mobile.app.ext.setSafeOnClickListener
import com.itcluster.mobile.app.presentation.view.MainActivity
import com.itcluster.mobile.app.presentation.view.loading.LoadingView
import com.itcluster.mobile.feature.list.di.AuthFactory
import com.itcluster.mobile.feature.list.model.state.LoginState
import com.itcluster.mobile.feature.list.presentation.AuthVm
import dagger.hilt.android.AndroidEntryPoint
import dev.icerock.moko.mvvm.MvvmFragment
import dev.icerock.moko.mvvm.createViewModelFactory
import dev.icerock.moko.mvvm.livedata.addCloseableObserver
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : MvvmFragment<FragmentAuthBinding, AuthVm>() {

    override val layoutId: Int = R.layout.fragment_auth
    override val viewModelVariableId: Int = BR.viewModel
    override val viewModelClass = AuthVm::class.java

    private val loadingView: LoadingView by lazy { requireView().findViewById(R.id.loading) }

    private val handler = Handler(Looper.getMainLooper())

    @Inject
    lateinit var factory: AuthFactory

    override fun viewModelFactory(): ViewModelProvider.Factory =
        createViewModelFactory { factory.createAuthViewModel() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingView.isVisible = true
        viewModel.auth.addCloseableObserver { loginState ->
            when (loginState) {
                is LoginState.Authorized.UnAuthorized -> bindUnAuthorizedState()
                is LoginState.Authorized.SuccessAuthorized -> {
                    handler.post {
                        (activity as MainActivity).navController.navigate(R.id.action_to_main)
                    }
                }
                is LoginState.LoginFirst.Companies -> {
                    loadingView.isVisible = false
                    (activity as MainActivity).navController.navigate(R.id.action_to_companies)
                }
                is LoginState.Error -> {
                    loadingView.isVisible = false
                    when (loginState) {
                        is LoginState.Error.Login -> binding.tilLogin.error = loginState.message
                        is LoginState.Error.Password -> binding.tilPassword.error = loginState.message
                        is LoginState.Error.Unknown -> {
                            LogSniffer.addLog(TAG + loginState.throwable.toString())
                            showToast(loginState.message)
                        }
                    }

                }
                else -> {}
            }
        }

    }

    private fun bindUnAuthorizedState() {
        loadingView.isVisible = false
        with(binding) {
            scrollableView.isVisible = true
            etPassword.doOnTextChanged { _, _, _, _ ->
                checkAuthButtonEnable()
            }
            etLogin.doOnTextChanged { _, _, _, _ ->
                checkAuthButtonEnable()
            }
            submitButton.setSafeOnClickListener {
                tilLogin.error = EMPTY_SEPARATOR
                tilPassword.error = EMPTY_SEPARATOR
                loadingView.isVisible = true
                hideKeyboard()
                viewModel?.companiesListRequest(etLogin.text.toString(), etPassword.text.toString())
            }
            checkAuthButtonEnable()
        }
    }

    private fun checkAuthButtonEnable() {
        binding.apply {
            submitButton.isEnabled =
                !(etLogin.text.isNullOrEmpty() || etPassword.text.isNullOrEmpty())
        }
    }

    private fun showToast(message: String?) {
        message?.also {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private const val EMPTY_SEPARATOR = ""

        private const val TAG = "AuthFragment: "
    }

}