package com.haythamayyash.androidunittestingcourse

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CalculatorTest {
    lateinit var sut: Calculator

    @Before
    fun setUp() {
        sut = Calculator()
    }

    @Test
    fun testAdd() {
        val result = sut.add(3, 4)
        assertEquals(7, result)
    }

    @Test
    fun `when server error expect post not cached`() {
        //your test
    }
}