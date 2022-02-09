package com.haythamayyash.androidunittestingcourse

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class StringDuplicatorTest {
    private lateinit var sut: StringDuplicator

    @Before
    fun setUp() {
        sut = StringDuplicator()
    }

    @Test
    fun duplicateString_emptyString_emptyStringReturned() {
        val text = sut.duplicateString("")
        assertEquals("", text)
    }

    @Test
    fun duplicateString_oneChar_duplicatedStringReturned() {
        val text = sut.duplicateString("a")
        assertEquals("aa",text)
    }

    @Test
    fun duplicateString_longString_duplicatedStringReturned() {
        val text = sut.duplicateString("Haitham")
        assertEquals("HaithamHaitham", text)
    }
}