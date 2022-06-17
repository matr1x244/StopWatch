package com.example.stopwatch

interface TimestampProvider {
    fun getMilliseconds(): Long
}