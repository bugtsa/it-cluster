package com.itcluster.mobile.feature.list.model

import com.itcluster.mobile.presentation.models.CompanyModel

interface CompaniesStore {

    var login: String
    var password: String
    var companies: MutableList<CompanyModel>
}