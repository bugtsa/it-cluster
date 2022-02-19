package com.itcluster.mobile.app.presentation.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.itcluster.mobile.app.R
import dev.icerock.moko.mvvm.MvvmFragment
import dev.icerock.moko.mvvm.createViewModelFactory
import com.itcluster.mobile.app.BR
import com.itcluster.mobile.app.databinding.FragmentMainPageBinding
import com.itcluster.mobile.app.ext.log.LogSniffer
import com.itcluster.mobile.app.ext.recycler.BaseDelegationAdapter
import com.itcluster.mobile.app.models.WalletUiState
import com.itcluster.mobile.app.models.WalletUiState.Companion.toState
import com.itcluster.mobile.app.presentation.view.WalletAdapterDelegates
import com.itcluster.mobile.app.presentation.view.loading.LoadingView
import com.itcluster.mobile.feature.list.di.MainPageFactory
import com.itcluster.mobile.feature.list.model.state.WalletState
import com.itcluster.mobile.feature.list.presentation.MainPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import dev.icerock.moko.mvvm.livedata.addCloseableObserver
import javax.inject.Inject

@AndroidEntryPoint
class MainPageFragment : MvvmFragment<FragmentMainPageBinding, MainPageViewModel>() {

    override val layoutId: Int = R.layout.fragment_main_page
    override val viewModelVariableId: Int = BR.viewModel
    override val viewModelClass = MainPageViewModel::class.java

    @Inject
    lateinit var factory: MainPageFactory

    private val adapter by lazy { createAdapter() }

    override fun viewModelFactory(): ViewModelProvider.Factory =
        createViewModelFactory { factory.createMainPageModel() }

    private val loadingView: LoadingView by lazy { requireView().findViewById(R.id.loading) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.stateWallet.addCloseableObserver { state ->
            when (state) {
                WalletState.NoState -> {

                }
                is WalletState.SuccessWallet -> {
                    loadingView.isVisible = false
                    fillDataAdapter(state.wallet.map { it.toState() })
                }
                is WalletState.Error.Unknown -> {
                    LogSniffer.addLog(TAG + state.throwable.toString())
                    showToast(state.message)
                }
            }
        }
        setupViews()
    }

    private fun setupViews() {
        binding.wallets.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@MainPageFragment.adapter
        }
    }

    private fun createAdapter() = BaseDelegationAdapter(
        WalletAdapterDelegates.walletAdapterDelegate(
            ::navToMainPage
        )
    )

    private fun navToMainPage(walletId: Long) {
        loadingView.isVisible = true
//        viewModel.authTokenRequest(companyId)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fillDataAdapter(wallets: List<WalletUiState>) {
        adapter.items = wallets
        adapter.notifyDataSetChanged()
    }

    private fun showToast(message: String?) {
        message?.also {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }
    }

    companion object {

        private const val TAG = "MainPageFragment: "
    }

}