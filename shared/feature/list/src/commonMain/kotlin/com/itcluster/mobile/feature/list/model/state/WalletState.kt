package com.itcluster.mobile.feature.list.model.state

import com.itcluster.mobile.presentation.models.WalletModel

sealed class WalletState {

    object NoState: WalletState()

    class SuccessWallet(
        val wallet: List<WalletModel>
    ): WalletState()
}