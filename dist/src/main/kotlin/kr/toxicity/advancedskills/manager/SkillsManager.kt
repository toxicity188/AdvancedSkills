package kr.toxicity.advancedskills.manager

import kr.toxicity.advancedskills.pack.PackData

interface SkillsManager {
    fun start() {}
    fun preReload() {}
    fun reload(data: PackData)
    fun postReload() {}
    fun end() {}
}