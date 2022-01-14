package com.itcluster.mobile.app.presentation.screens

import android.os.Bundle
import android.view.View
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
        viewModel.auth.addCloseableObserver { authString ->
            takeIf { authString.isNotEmpty() }?.also {
                (activity as MainActivity).navController.navigate(R.id.action_to_main)
                loadingView.isVisible = false
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

}