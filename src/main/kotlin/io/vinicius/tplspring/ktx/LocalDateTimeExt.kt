package io.vinicius.tplspring.ktx

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

val LocalDateTime.date: Date
    get() = Date.from(this.atZone(ZoneId.systemDefault()).toInstant())