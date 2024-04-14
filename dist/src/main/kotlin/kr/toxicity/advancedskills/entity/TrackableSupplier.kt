package kr.toxicity.advancedskills.entity

import kr.toxicity.advancedskills.equation.EquationLocation

interface TrackableSupplier {
    fun supply(parent: SkillEntity, scale: Int, equationLocation: EquationLocation): List<SkillEntity>
}