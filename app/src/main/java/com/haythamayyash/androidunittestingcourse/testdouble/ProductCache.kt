package com.haythamayyash.androidunittestingcourse.testdouble

interface ProductCache {
    fun cacheProduct(product: Product)
    fun getProduct(): Product
}