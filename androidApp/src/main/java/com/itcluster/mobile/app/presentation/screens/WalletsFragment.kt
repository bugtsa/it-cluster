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
import com.itcluster.mobile.app.databinding.FragmentWalletsBinding
import com.itcluster.mobile.app.ext.log.LogSniffer
import com.itcluster.mobile.app.ext.recycler.BaseDelegationAdapter
import com.itcluster.mobile.app.models.WalletUiState
import com.itcluster.mobile.app.models.WalletUiState.Companion.toState
import com.itcluster.mobile.app.presentation.view.MainActivity
import com.itcluster.mobile.app.presentation.view.adapters.WalletAdapterDelegates
import com.itcluster.mobile.feature.list.di.WalletsFactory
import com.itcluster.mobile.feature.list.model.state.WalletState.*
import com.itcluster.mobile.feature.list.presentation.WalletsVm
import dagger.hilt.android.AndroidEntryPoint
import dev.icerock.moko.mvvm.livedata.addCloseableObserver
import javax.inject.Inject

@AndroidEntryPoint
class WalletsFragment : MvvmFragment<FragmentWalletsBinding, WalletsVm>() {

    override val layoutId: Int = R.layout.fragment_wallets
    override val viewModelVariableId: Int = BR.viewModel
    override val viewModelClass = WalletsVm::class.java

    @Inject
    lateinit var factory: WalletsFactory

    private val adapter by lazy { createAdapter() }

    override fun viewModelFactory(): ViewModelProvider.Factory =
        createViewModelFactory { factory.createWalletsModel() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.stateWallet.addCloseableObserver { state ->
            when (state) {
                NoState -> {}
                is SuccessWallet -> {
                    binding.loading.isVisible = false
                    fillDataAdapter(state.wallet.map { it.toState() })
                }
                is Error.Unknown -> {
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
            adapter = this@WalletsFragment.adapter
        }
    }

    private fun createAdapter() = BaseDelegationAdapter(
        WalletAdapterDelegates.walletAdapterDelegate(
            ::navToTransactionsPage
        )
    )

    private fun navToTransactionsPage(billId: Long) {
        binding.loading.isVisible = true
        val action = WalletsFragmentDirections.actionToTransactions(TransactionsSpecs(billId))
        (activity as MainActivity).navController.navigate(action)
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

        private const val TAG = "WalletsFragment: "
    }

}