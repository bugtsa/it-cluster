package com.itcluster.mobile.app.presentation.view

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.itcluster.mobile.app.R
import com.itcluster.mobile.app.databinding.ItemWalletBinding
import com.itcluster.mobile.app.ext.recycler.RecyclerViewItem
import com.itcluster.mobile.app.ext.setSafeOnClickListener
import com.itcluster.mobile.app.models.WalletUiState

typealias OnWalletClick = (Long) -> Unit

object WalletAdapterDelegates {
    fun walletAdapterDelegate(onWalletClick: OnWalletClick) =
        adapterDelegateViewBinding<WalletUiState, RecyclerViewItem, ItemWalletBinding>(
            { layoutInflater, root -> ItemWalletBinding.inflate(layoutInflater, root, false) }
        ) {
            bind {
                binding.apply {
                    image.text = context.getString(R.string.company_image_field, item.image)
                    name.text = context.getString(R.string.company_title_field, item.name)
                    quantity.text = context.getString(R.string.wallet_amount_field, item.amount)
                    walletRoot.setSafeOnClickListener {
                        onWalletClick.invoke(item.id)
                    }
                }
            }
        }
}