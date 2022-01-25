package com.itcluster.mobile.app.presentation.screens

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.*
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.itcluster.mobile.app.BR
import com.itcluster.mobile.app.R
import com.itcluster.mobile.app.databinding.FragmentAuthBinding
import com.itcluster.mobile.app.ext.hideKeyboard
import com.itcluster.mobile.app.ext.setSafeOnClickListener
import com.itcluster.mobile.app.presentation.view.MainActivity
import com.itcluster.mobile.app.presentation.view.loading.LoadingView
import com.itcluster.mobile.feature.list.di.AuthFactory
import com.itcluster.mobile.feature.list.model.AuthState
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

    @Inject
    lateinit var factory: AuthFactory

    override fun viewModelFactory(): ViewModelProvider.Factory =
        createViewModelFactory { factory.createAuthViewModel() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingView.isVisible = false
        viewModel.auth.addCloseableObserver { authState ->
            when (authState) {
                is AuthState.Success -> {
                    loadingView.isVisible = false
                    (activity as MainActivity).navController.navigate(R.id.action_to_main)
                }
                is AuthState.Error -> {
                    loadingView.isVisible = false
                    when (authState) {
                        is AuthState.Error.Login -> binding.tilLogin.error = authState.message
                        is AuthState.Error.Password -> binding.tilPassword.error = authState.message
                        is AuthState.Error.Unknown -> showToast(authState.message)
                    }

                }
                AuthState.NoAction -> {}
            }
        }

        with(binding) {
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
                viewModel?.requestAuth(etLogin.text.toString(), etPassword.text.toString())
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
    }

}