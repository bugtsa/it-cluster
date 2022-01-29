package com.itcluster.mobile.domain.repository

import com.itcluster.mobile.domain.Constants.Companion.EMPTY_SEPARATOR
import com.itcluster.mobile.presentation.models.CompanyModel

class CompaniesRepository {

    var login: String = EMPTY_SEPARATOR

    var password: String = EMPTY_SEPARATOR

    val companies: MutableList<CompanyModel> = mutableListOf()
}