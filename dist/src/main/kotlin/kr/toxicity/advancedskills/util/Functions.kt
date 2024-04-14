package kr.toxicity.advancedskills.util

fun <T> T?.ifNull(message: String): T & Any = this ?: throw RuntimeException(message)