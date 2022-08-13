package com.haythamayyash.androidunittestingcourse.testdouble

import com.haythamayyash.androidunittestingcourse.helper.capture
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner


private const val ID = "My_ID"
private val SUCCESS_PRODUCT = Product(ID, "product name", 10.5)

@RunWith(MockitoJUnitRunner::class)
class FetchProductUseCaseTestMockingFramework {

    private lateinit var sut: FetchProductUseCase

    @Mock
    private lateinit var productServiceMock: ProductService

    @Mock
    private lateinit var productCacheMock: ProductCache

    @Mock
    private lateinit var productAnalyticManagerMock: ProductAnalyticManager

    @Captor
    private lateinit var stringCaptor: ArgumentCaptor<String>

    @Captor
    private lateinit var productCaptor: ArgumentCaptor<Product>

    @Before
    fun setUp() {
        sut = FetchProductUseCase(productServiceMock, productCacheMock, productAnalyticManagerMock)
    }

    @Test
    fun getProduct_success_idPassedToServiceCorrectly() {
        sut.fetchProduct(ID)
        verify(productServiceMock, times(1)).getProduct(capture(stringCaptor))
        assertEquals(ID, stringCaptor.value)
    }

    @Test
    fun getProduct_success_noInteractionOnLogFailure() {
        prepareSuccess()
        sut.fetchProduct(ID)
        verifyNoMoreInteractions(productAnalyticManagerMock)
    }

    @Test
    fun getProduct_generalError_logFailureShouldCalledOnce() {
        prepareGeneralError()
        sut.fetchProduct(ID)
        verify(productAnalyticManagerMock, times(1)).logFailure()
    }

    @Test
    fun getProduct_serverError_logFailureShouldCalledOnce() {
        prepareServerError()
        sut.fetchProduct(ID)
        verify(productAnalyticManagerMock, times(1)).logFailure()
    }

    @Test
    fun getProduct_success_productShouldCached() {
        prepareSuccess()
        sut.fetchProduct(ID)
        verify(productCacheMock).cacheProduct(capture(productCaptor))
        assertEquals(SUCCESS_PRODUCT, productCaptor.value)
    }

    @Test
    fun getProduct_generalError_productShouldNotCached() {
        prepareGeneralError()
        sut.fetchProduct(ID)
        verifyNoMoreInteractions(productCacheMock)
    }

    @Test
    fun getProduct_serverError_productShouldNotCached() {
        prepareServerError()
        sut.fetchProduct(ID)
        verifyNoMoreInteractions(productCacheMock)
    }

    @Test
    fun getProduct_success_successReturned() {
        prepareSuccess()
        val result = sut.fetchProduct(ID)
        assertEquals(FetchProductUseCase.Result.Success(SUCCESS_PRODUCT), result)
    }

    @Test
    fun getProduct_generalError_failureReturned() {
        prepareGeneralError()
        val result = sut.fetchProduct(ID)
        assertEquals(FetchProductUseCase.Result.Failure, result)
    }

    @Test
    fun getProduct_serverError_failureReturned() {
        prepareServerError()
        val result = sut.fetchProduct(ID)
        assertEquals(FetchProductUseCase.Result.Failure, result)
    }

    @Test
    fun getProduct_NetworkError_NetworkError() {
        prepareNetworkError()
        val result = sut.fetchProduct(ID)
        assertEquals(FetchProductUseCase.Result.NetworkError, result)
    }

    private fun prepareSuccess() {
        `when`(productServiceMock.getProduct(ID))
            .thenReturn(ProductService.ServiceResult.Success(SUCCESS_PRODUCT))
    }

    private fun prepareGeneralError() {
        `when`(productServiceMock.getProduct(ID))
            .thenReturn(ProductService.ServiceResult.GeneralError)
    }

    private fun prepareServerError() {
        `when`(productServiceMock.getProduct(ID))
            .thenReturn(ProductService.ServiceResult.ServerError)
    }

    private fun prepareNetworkError() {
        doThrow(NetworkErrorException()).`when`(productServiceMock).getProduct(ID)
    }
}
