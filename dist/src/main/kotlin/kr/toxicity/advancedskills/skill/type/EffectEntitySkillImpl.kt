package kr.toxicity.advancedskills.skill.type

import kr.toxicity.advancedskills.api.skill.type.EffectEntitySkill
import kr.toxicity.advancedskills.api.skill.type.EffectEntitySkill.Orient
import kr.toxicity.advancedskills.entity.SkillEntityConfiguration
import kr.toxicity.advancedskills.manager.EntityManager
import org.bukkit.Location
import org.bukkit.entity.Entity

class EffectEntitySkillImpl(
    private val configuration: SkillEntityConfiguration,
    private val orient: Orient
): EffectEntitySkill {
    override fun cast(entity: Entity) {
        configuration.create(if (orient == Orient.CASTER) EntityManager.entity(entity) else EntityManager.location(entity.world, entity.location))
    }

    override fun cast(caster: Entity, target: Entity) {
        configuration.create(if (orient == Orient.CASTER) EntityManager.entity(target) else EntityManager.location(target.world, target.location))
    }

    override fun cast(entity: Entity, location: Location) {
        configuration.create(if (orient == Orient.CASTER) EntityManager.entity(entity) else EntityManager.location(location.world ?: return, location))
    }
}