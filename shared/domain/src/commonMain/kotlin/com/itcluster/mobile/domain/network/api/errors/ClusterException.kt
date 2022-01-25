package com.itcluster.mobile.domain.network.api.errors

class ClusterException(
    val error: ErrorClusterDto
) : Exception()