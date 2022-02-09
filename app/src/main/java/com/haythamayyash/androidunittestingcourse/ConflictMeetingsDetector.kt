package com.haythamayyash.androidunittestingcourse

import java.text.ParseException
import java.text.SimpleDateFormat
import kotlin.jvm.Throws

class ConflictMeetingsDetector {
   private val parser = SimpleDateFormat("HH:mm")
    init {
        parser.isLenient = false
    }
    /**
     * time should be in 24 hour system
     */
    @Throws(ParseException::class)
    fun haveMeetingsConflict(meetingInterval1: MeetingInterval, meetingInterval2: MeetingInterval): Boolean {
        return parser.parse(meetingInterval1.endTime) > parser.parse(meetingInterval2.startTime) &&
                parser.parse(meetingInterval1.startTime) < parser.parse(meetingInterval2.endTime)
    }
}


data class MeetingInterval(val startTime: String, val endTime: String)