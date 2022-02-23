package com.itcluster.mobile.app.presentation.view.adapters

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.itcluster.mobile.app.databinding.ItemCompanyBinding
import com.itcluster.mobile.app.ext.glide.networkLoadImage
import com.itcluster.mobile.app.ext.setSafeOnClickListener
import com.itcluster.mobile.app.ext.recycler.RecyclerViewItem
import com.itcluster.mobile.app.models.CompanyState

typealias OnFieldClick = (Long) -> Unit

object CompanyAdapterDelegates {
    fun companyAdapterDelegate(onCompanyFieldClick: OnFieldClick) =
        adapterDelegateViewBinding<CompanyState, RecyclerViewItem, ItemCompanyBinding>(
            { layoutInflater, root -> ItemCompanyBinding.inflate(layoutInflater, root, false) }
        ) {
            bind {
                binding.apply {
                    ivPhoto.networkLoadImage(item.image)
                    name.text = item.name
                    companyRoot.setSafeOnClickListener {
                        onCompanyFieldClick.invoke(item.id)
                    }
                }
            }
        }
}
