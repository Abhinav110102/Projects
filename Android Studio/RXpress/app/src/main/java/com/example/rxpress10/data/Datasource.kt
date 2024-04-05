package com.example.rxpress10.data

import com.example.rxpress10.R
import com.example.rxpress10.model.Times

class Datasource {

    fun loadTimes(): List<Times> {
        return listOf<Times>(
            Times(R.string.a1, R.string.event1, R.string.event2, R.string.event3),
            Times(R.string.a2, R.string.event1, R.string.event2, R.string.event3),
            Times(R.string.a3, R.string.event1, R.string.event2, R.string.event3),
            Times(R.string.a4, R.string.event1, R.string.event2, R.string.event3),
            Times(R.string.a5, R.string.event1, R.string.event2, R.string.event3),
            Times(R.string.a6, R.string.event1, R.string.event2, R.string.event3),
            Times(R.string.a7, R.string.event1, R.string.event2, R.string.event3),
            Times(R.string.a8, R.string.event1, R.string.event2, R.string.event3),
            Times(R.string.a9, R.string.event1, R.string.event2, R.string.event3),
            Times(R.string.a10, R.string.event1, R.string.event2, R.string.event3),
            Times(R.string.a11, R.string.event1, R.string.event2, R.string.event3),
            Times(R.string.p12, R.string.event1, R.string.event2, R.string.event3),
            Times(R.string.p1, R.string.event1, R.string.event2, R.string.event3),
            Times(R.string.p2, R.string.event1, R.string.event2, R.string.event3),
            Times(R.string.p3, R.string.event1, R.string.event2, R.string.event3),
            Times(R.string.p4, R.string.event1, R.string.event2, R.string.event3),
            Times(R.string.p5, R.string.event1, R.string.event2, R.string.event3),
            Times(R.string.p6, R.string.event1, R.string.event2, R.string.event3),
            Times(R.string.p7, R.string.event1, R.string.event2, R.string.event3),
            Times(R.string.p8, R.string.event1, R.string.event2, R.string.event3),
            Times(R.string.p9, R.string.event1, R.string.event2, R.string.event3),
            Times(R.string.p10, R.string.event1, R.string.event2, R.string.event3),
            Times(R.string.p11, R.string.event1, R.string.event2, R.string.event3),
            Times(R.string.a12, R.string.event1, R.string.event2, R.string.event3),
        )
    }
}