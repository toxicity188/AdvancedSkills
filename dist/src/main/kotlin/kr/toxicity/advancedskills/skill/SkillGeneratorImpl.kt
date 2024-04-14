package kr.toxicity.advancedskills.skill

import kr.toxicity.advancedskills.api.skill.SkillGenerator
import kr.toxicity.advancedskills.api.skill.type.EffectEntitySkill
import kr.toxicity.advancedskills.api.skill.type.EffectEntitySkill.Orient
import kr.toxicity.advancedskills.manager.EntityManager
import kr.toxicity.advancedskills.skill.type.EffectEntitySkillImpl
import kr.toxicity.advancedskills.util.ifNull

class SkillGeneratorImpl: SkillGenerator {
    override fun effectEntity(name: String, orient: Orient): EffectEntitySkill {
        return EffectEntitySkillImpl(EntityManager.config(name).ifNull("this config doesn't exist: $name"), orient)
    }
}