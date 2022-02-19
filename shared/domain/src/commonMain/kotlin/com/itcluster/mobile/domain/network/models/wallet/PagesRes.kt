package com.itcluster.mobile.domain.network.models.wallet

import com.itcluster.mobile.presentation.models.WalletPageModel
import kotlinx.serialization.Serializable

@Serializable
data class PagesRes(

    val current: Int,

    val count: Int
) {

    companion object {

        fun PagesRes.toModel() = WalletPageModel(
            this.current, this.count
        )
    }
}