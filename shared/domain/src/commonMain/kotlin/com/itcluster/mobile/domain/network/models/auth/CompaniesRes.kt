package com.itcluster.mobile.domain.network.models.auth

import com.itcluster.mobile.presentation.models.CompanyModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CompaniesRes(

	@SerialName("is_2fa")
	val is2fa: Int,

	@SerialName("company_list")
	val companyList: List<CompanyListItem>
) {

	companion object {

		fun CompaniesRes.toModel(): List<CompanyModel> =
			companyList.map { item ->
				CompanyModel(
					item.image,
					item.name,
					item.id
				)
			}
	}
}