package com.itcluster.mobile.app.models

import android.os.Parcelable
import com.itcluster.mobile.app.ext.recycler.RecyclerViewItem
import com.itcluster.mobile.presentation.models.CompanyModel
import kotlinx.android.parcel.Parcelize

@Parcelize
class CompanyState(
    val image: String,
    val name: String,
    val id: Long
): RecyclerViewItem, Parcelable {

    companion object {

        fun CompanyModel.toState() : CompanyState =
            CompanyState(
                image, name, id
            )
    }

    override fun getId() = id
}