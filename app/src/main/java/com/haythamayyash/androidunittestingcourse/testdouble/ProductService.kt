package com.haythamayyash.androidunittestingcourse.testdouble


interface ProductService {

    sealed class ServiceResult {
        data class Success(val product: Product) : ServiceResult()
        object GeneralError : ServiceResult()
        object ServerError : ServiceResult()
    }

    @Throws(NetworkErrorException::class)
    fun getProduct(id: String): ServiceResult
}
