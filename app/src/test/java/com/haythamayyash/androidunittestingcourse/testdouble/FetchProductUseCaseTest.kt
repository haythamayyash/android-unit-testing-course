package com.haythamayyash.androidunittestingcourse.testdouble


import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
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

    //id passed correctly to service
    @Test
    fun getProduct_success_idPassedToServiceCorrectly() {
        sut.fetchProduct(ID)
        assertThat(productServiceTd.id, `is`("ll"))
    }

    //if getProduct success - product should cached
    @Test
    fun getProduct_success_productShouldCached() {
        sut.fetchProduct("jj")
        assertThat(productCacheTd.getProduct(), `is`(SUCCESS_PRODUCT))
    }

    //if getProduct general error - product should not cached
    @Test
    fun getProduct_generalError_productShouldNotCached() {
        productServiceTd.isGeneralError = true
        sut.fetchProduct(ID)
        assertThat(productCacheTd.getProduct(), `is`(NON_INITIALIZED_PRODUCT))
    }

    //if getProduct server error - product should not cached
    @Test
    fun getProduct_serverError_productShouldNotCached() {
        productServiceTd.isServerError = true
        sut.fetchProduct(ID)
        assertThat(productCacheTd.getProduct(), `is`(NON_INITIALIZED_PRODUCT))
    }

    //if getProduct success - logFailure should not call
    @Test
    fun getProduct_success_noInteractionOnLogFailure() {
        sut.fetchProduct("ll")
        assertThat(productAnalyticManagerTd.failureCounter, `is`(0))
    }

    //if getProduct general error - logFailure should called once
    @Test
    fun getProduct_generalError_logFailureShouldCalledOnce() {
        productServiceTd.isGeneralError = false
        sut.fetchProduct(ID)
        assertThat(productAnalyticManagerTd.failureCounter, `is`(1))
    }

    //if getProduct server error - logFailure should called once
    @Test
    fun getProduct_serverError_logFailureShouldCalledOnce() {
        productServiceTd.isServerError = true
        sut.fetchProduct(ID)
        assertThat(productAnalyticManagerTd.failureCounter, `is`(1))
    }

    //if getProduct success - success returned
    @Test
    fun getProduct_success_successReturned() {
        val result = sut.fetchProduct(ID)
        assertThat(result, `is`(FetchProductUseCase.Result.Success(SUCCESS_PRODUCT)))
    }

    //if getProduct general error - failure returned
    @Test
    fun getProduct_generalError_failureReturned() {
        productServiceTd.isGeneralError = true
        val result = sut.fetchProduct(ID)
        assertThat(result, `is`(FetchProductUseCase.Result.Failure))
    }

    //if getProduct server error  - failure returned
    @Test
    fun getProduct_serverError_failureReturned() {
        productServiceTd.isServerError = false
        val result = sut.fetchProduct(ID)
        assertThat(result, `is`(FetchProductUseCase.Result.Failure))
    }

    @Test
    fun getProduct_NetworkError_NetworkError() {
        productServiceTd.isNetworkError = false
        val result = sut.fetchProduct(ID)
        assertThat(result, `is`(FetchProductUseCase.Result.NetworkError))

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