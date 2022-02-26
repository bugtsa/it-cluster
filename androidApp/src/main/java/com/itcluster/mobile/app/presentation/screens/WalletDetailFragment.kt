package com.itcluster.mobile.app.presentation.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.itcluster.mobile.app.BR
import com.itcluster.mobile.app.R
import com.itcluster.mobile.app.databinding.FragmentTransactionsBinding
import com.itcluster.mobile.app.ext.glide.networkLoadImage
import com.itcluster.mobile.app.ext.log.LogSniffer
import com.itcluster.mobile.app.ext.recycler.BaseDelegationAdapter
import com.itcluster.mobile.app.models.TransactionUIState
import com.itcluster.mobile.app.models.TransactionUIState.Companion.toState
import com.itcluster.mobile.app.presentation.view.adapters.TransactionAdapterDelegates
import com.itcluster.mobile.feature.list.di.WalletDetailFactory
import com.itcluster.mobile.feature.list.model.state.DetailWalletState.*
import com.itcluster.mobile.feature.list.presentation.WalletDetailVm
import com.itcluster.mobile.presentation.models.WalletItemModel
import dagger.hilt.android.AndroidEntryPoint
import dev.icerock.moko.mvvm.MvvmFragment
import dev.icerock.moko.mvvm.createViewModelFactory
import dev.icerock.moko.mvvm.livedata.addCloseableObserver
import javax.inject.Inject

@AndroidEntryPoint
class WalletDetailFragment : MvvmFragment<FragmentTransactionsBinding, WalletDetailVm>() {

    override val layoutId: Int = R.layout.fragment_transactions
    override val viewModelVariableId: Int = BR.viewModel
    override val viewModelClass = WalletDetailVm::class.java

    @Inject
    lateinit var factory: WalletDetailFactory

    private val args: WalletDetailFragmentArgs by navArgs()
    private val specs by lazy { args.specs }

    override fun viewModelFactory(): ViewModelProvider.Factory =
        createViewModelFactory { factory.createWalletDetailModel(specs.billId) }

    private val adapter by lazy { createAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.stateDetailWallet.addCloseableObserver { state ->
            when (state) {
                NoState -> {

                }
                is SuccessDetail -> {
                    binding.loading.isVisible = false
                    bindDetails(state.details)
                }
                is SuccessTransactions -> {
                    binding.loading.isVisible = false
                    fillDataAdapter(state.transactions.map { it.toState() })
                }
                is Error.Unknown -> {
                    binding.loading.isVisible = false
                    LogSniffer.addLog(TAG + state.throwable.toString())
                    showToast(state.message)
                }
            }
        }
        setupViews()
    }

    private fun bindDetails(details: WalletItemModel) {
        binding.apply {
            ivPhoto.networkLoadImage(details.bill.currency.image)
            name.text = requireContext().getString(R.string.company_title_field, details.bill.name)
            quantity.text = requireContext().getString(R.string.wallet_amount_field, details.wallet.amount)
            codeCurrency.text = details.bill.currency.code
        }
    }

    private fun setupViews() {
        binding.transactions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@WalletDetailFragment.adapter
        }
    }

    private fun createAdapter() = BaseDelegationAdapter(
        TransactionAdapterDelegates.transactionAdapterDelegate(
            ::navToTransactionsPage
        )
    )

    private fun navToTransactionsPage(billId: Long) {}

    @SuppressLint("NotifyDataSetChanged")
    private fun fillDataAdapter(transactions: List<TransactionUIState>) {
        adapter.items = transactions
        adapter.notifyDataSetChanged()
    }

    private fun showToast(message: String?) {
        message?.also {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }
    }

    companion object {

        private const val TAG = "TransactionsFragment: "
    }

}