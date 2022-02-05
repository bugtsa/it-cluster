package com.itcluster.mobile.domain.network.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class CompanyListItem(

	val image: String?,
	val name: String,
	val id: Long
)