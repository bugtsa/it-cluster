package com.itcluster.mobile.domain.network.api.errors

class ErrorClusterDto(
    val code: Int,

    val name: String,

    val message: AuthErrorDto,

    val status: Int
) {

    companion object {

        fun ErrorBaseRes.toDto(messageError: MessageErrorRes): ErrorClusterDto {
            val resultError = StringBuilder()
            val resultType = StringBuilder()
            messageError.fields?.forEach { fieldName ->
                messageError.errors?.get(fieldName)?.forEach { messageType ->
                    resultType.append(fieldName)
                    resultError.append(messageType)
                }
            }
            val errorDto = when (resultType.toString()) {
                "password" -> AuthErrorDto.Password(resultError.toString())
                else -> AuthErrorDto.Login(resultError.toString())
            }

            return ErrorClusterDto(
                code ?: INT_DEFAULT,
                name ?: STRING_DEFAULT,
                errorDto,
                status ?: INT_DEFAULT
            )
        }

        private const val INT_DEFAULT = 0
        private const val STRING_DEFAULT = ""
    }
}