package com.itcluster.mobile.app.presentation.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.itcluster.mobile.app.R
import com.itcluster.mobile.presentation.models.CompanyModel

class CompaniesRvAdapter (var companies: List<CompanyModel>) : RecyclerView.Adapter<CompaniesRvAdapter.CompanyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.item_company, parent, false)
            .run(::CompanyViewHolder)
    }

    override fun getItemCount(): Int = companies.count()

    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        holder.bindData(companies[position])
    }

    inner class CompanyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image = itemView.findViewById<TextView>(R.id.image)
        private val name = itemView.findViewById<TextView>(R.id.name)

        fun bindData(launch: CompanyModel) {
            val ctx = itemView.context
            image.text = ctx.getString(R.string.mission_name_field, launch.image)
            name.text = ctx.getString(R.string.launch_year_field, launch.name)
        }
    }
}