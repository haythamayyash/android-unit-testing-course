package com.haythamayyash.androidunittestingcourse

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ConflictMeetingsDetectorTest {

    private lateinit var sut: ConflictMeetingsDetector

    @Before
    fun setUp() {
        sut = ConflictMeetingsDetector()
    }

    @Test
    fun haveMeetingsConflict_time1BeforeTime2_falseReturned() {
        val haveMeetingsConflict = sut.haveMeetingsConflict(
            meetingInterval1 = MeetingInterval("9:00", "10:00"),
            meetingInterval2 = MeetingInterval("12:00", "13:00")
        )
        assertEquals(false, haveMeetingsConflict)
    }

    @Test
    fun haveMeetingsConflict_time1AfterTime2_falseReturned() {
        val haveMeetingsConflict = sut.haveMeetingsConflict(
            meetingInterval1 = MeetingInterval("12:00", "13:00"),
            meetingInterval2 = MeetingInterval("9:00", "10:00")
        )
        assertEquals(false, haveMeetingsConflict)
    }

    @Test
    fun haveMeetingsConflict_time1OverlapTime2FromStart_trueReturned() {
        val haveMeetingsConflict = sut.haveMeetingsConflict(
            meetingInterval1 = MeetingInterval("12:00", "13:00"),
            meetingInterval2 = MeetingInterval("12:45", "14:00")
        )
        assertEquals(true, haveMeetingsConflict)
    }

    @Test
    fun haveMeetingsConflict_time1OverlapTime2FromEnd_trueReturned() {
        val haveMeetingsConflict = sut.haveMeetingsConflict(
            meetingInterval1 = MeetingInterval("13:45", "15:00"),
            meetingInterval2 = MeetingInterval("12:45", "14:00")
        )
        assertEquals(true, haveMeetingsConflict)
    }

    @Test
    fun haveMeetingsConflict_time1PartOfTime2_trueReturned() {
        val haveMeetingsConflict = sut.haveMeetingsConflict(
            meetingInterval1 = MeetingInterval("13:00", "13:30"),
            meetingInterval2 = MeetingInterval("12:00", "14:00")
        )
        assertEquals(true, haveMeetingsConflict)
    }

    @Test
    fun haveMeetingsConflict_time2PartOfTime1_trueReturned() {
        val haveMeetingsConflict = sut.haveMeetingsConflict(
            meetingInterval1 = MeetingInterval("12:00", "14:00"),
            meetingInterval2 = MeetingInterval("13:00", "13:30")
        )
        assertEquals(true, haveMeetingsConflict)
    }

    @Test
    fun haveMeetingsConflict_time1MatchTime2_trueReturned() {
        val haveMeetingsConflict = sut.haveMeetingsConflict(
            meetingInterval1 = MeetingInterval("12:00", "14:00"),
            meetingInterval2 = MeetingInterval("12:00", "14:30")
        )
        assertEquals(true, haveMeetingsConflict)
    }

    @Test
    fun haveMeetingsConflict_time1AdjacentTime2FromStart_falseReturned() {
        val haveMeetingsConflict = sut.haveMeetingsConflict(
            meetingInterval1 = MeetingInterval("12:00", "14:00"),
            meetingInterval2 = MeetingInterval("14:00", "14:30")
        )
        assertEquals(false, haveMeetingsConflict)
    }

    @Test
    fun haveMeetingsConflict_time1AdjacentTime2FromEnd_falseReturned() {
        val haveMeetingsConflict = sut.haveMeetingsConflict(
            meetingInterval1 = MeetingInterval("14:30", "15:00"),
            meetingInterval2 = MeetingInterval("14:00", "14:30")
        )
        assertEquals(false, haveMeetingsConflict)
    }

}