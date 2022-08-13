package com.haythamayyash.androidunittestingcourse.testdouble


import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

private const val ID = "My_ID"
private val SUCCESS_PRODUCT = Product(ID, "product name", 10.5)
private val NON_INITIALIZED_PRODUCT = Product()

class FetchProductUseCaseTest {

    private lateinit var sut: FetchProductUseCase
    private lateinit var productServiceTd: ProductServiceTd
    private lateinit var productCacheTd: ProductCacheTd
    private lateinit var productAnalyticManagerTd: ProductAnalyticManagerTd

    @Before
    fun setUp() {
        productServiceTd = ProductServiceTd()
        productCacheTd = ProductCacheTd()
        productAnalyticManagerTd = ProductAnalyticManagerTd()
        sut = FetchProductUseCase(productServiceTd, productCacheTd, productAnalyticManagerTd)
    }


    @Test
    fun getProduct_success_idPassedToServiceCorrectly() {
        sut.fetchProduct(ID)
        assertEquals(ID, productServiceTd.id)
    }

    @Test
    fun getProduct_success_noInteractionOnLogFailure() {
        sut.fetchProduct(ID)
        assertEquals(0, productAnalyticManagerTd.failureCounter)
    }

    @Test
    fun getProduct_generalError_logFailureShouldCalledOnce() {
        productServiceTd.isGeneralError = true
        sut.fetchProduct(ID)
        assertEquals(1, productAnalyticManagerTd.failureCounter)
    }

    @Test
    fun getProduct_serverError_logFailureShouldCalledOnce() {
        productServiceTd.isServerError = true
        sut.fetchProduct(ID)
        assertEquals(1, productAnalyticManagerTd.failureCounter)
    }

    @Test
    fun getProduct_success_productShouldCached() {
        sut.fetchProduct("jj")
        assertEquals(SUCCESS_PRODUCT, productCacheTd.getProduct())
    }

    @Test
    fun getProduct_generalError_productShouldNotCached() {
        productServiceTd.isGeneralError = true
        sut.fetchProduct(ID)
        assertEquals(NON_INITIALIZED_PRODUCT, productCacheTd.getProduct())
    }

    @Test
    fun getProduct_serverError_productShouldNotCached() {
        productServiceTd.isServerError = true
        sut.fetchProduct(ID)
        assertEquals(NON_INITIALIZED_PRODUCT, productCacheTd.getProduct())
    }

    @Test
    fun getProduct_success_successReturned() {
        val result = sut.fetchProduct(ID)
        assertThat(result, `is`(FetchProductUseCase.Result.Success(SUCCESS_PRODUCT)))
    }

    @Test
    fun getProduct_generalError_failureReturned() {
        productServiceTd.isGeneralError = true
        val result = sut.fetchProduct(ID)
        assertEquals(FetchProductUseCase.Result.Failure, result)
    }

    @Test
    fun getProduct_serverError_failureReturned() {
        productServiceTd.isServerError = true
        val result = sut.fetchProduct(ID)
        assertEquals(FetchProductUseCase.Result.Failure, result)
    }

    @Test
    fun getProduct_NetworkError_NetworkError() {
        productServiceTd.isNetworkError = true
        val result = sut.fetchProduct(ID)
        assertEquals(FetchProductUseCase.Result.NetworkError, result)
    }

    class ProductServiceTd : ProductService {
        var isNetworkError = false
        var isServerError = false
        var isGeneralError = false
        var id = ""

        override fun getProduct(id: String): ProductService.ServiceResult {
            this.id = id
            if (isGeneralError) {
                return ProductService.ServiceResult.GeneralError
            } else if (isServerError) {
                return ProductService.ServiceResult.ServerError
            } else if (isNetworkError) {
                throw NetworkErrorException()
            } else {
                return ProductService.ServiceResult.Success(SUCCESS_PRODUCT)
            }
        }

    }

    class ProductCacheTd : ProductCache {
        var mProduct = NON_INITIALIZED_PRODUCT
        override fun cacheProduct(product: Product) {
            mProduct = product
        }

        override fun getProduct(): Product {
            return mProduct
        }

    }

    class ProductAnalyticManagerTd : ProductAnalyticManager {
        var failureCounter = 0
        override fun logFailure() {
            failureCounter++
        }

    }
}