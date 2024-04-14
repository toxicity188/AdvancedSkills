package kr.toxicity.advancedskills.entity

interface EntityLocator {
    fun delay(): Long
    fun duration(): Long
    fun locate(): EntityLocation
}