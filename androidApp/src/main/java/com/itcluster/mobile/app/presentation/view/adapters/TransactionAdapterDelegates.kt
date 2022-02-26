package com.itcluster.mobile.app.presentation.view.adapters

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.itcluster.mobile.app.R
import com.itcluster.mobile.app.databinding.ItemTransactionBinding
import com.itcluster.mobile.app.ext.recycler.RecyclerViewItem
import com.itcluster.mobile.app.ext.setSafeOnClickListener
import com.itcluster.mobile.app.models.TransactionUIState

typealias OnTransactionClick = (Long) -> Unit

object TransactionAdapterDelegates {
    fun transactionAdapterDelegate(onWalletClick: OnTransactionClick) =
        adapterDelegateViewBinding<TransactionUIState, RecyclerViewItem, ItemTransactionBinding>(
            { layoutInflater, root -> ItemTransactionBinding.inflate(layoutInflater, root, false) }
        ) {
            bind {
                binding.apply {
                    type.text = item.type.humanValue
                    name.text = context.getString(R.string.company_title_field, item.comment)
                    quantity.text = context.getString(R.string.wallet_amount_field, item.amount)
                    codeCurrency.text = item.codeCurrency

                    walletRoot.setSafeOnClickListener {
                        onWalletClick.invoke(item.id)
                    }
                }
            }
        }
}