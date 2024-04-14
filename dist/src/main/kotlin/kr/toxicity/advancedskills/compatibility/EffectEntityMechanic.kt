package kr.toxicity.advancedskills.compatibility

import io.lumine.mythic.api.adapters.AbstractEntity
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.INoTargetSkill
import io.lumine.mythic.api.skills.ISkillMechanic
import io.lumine.mythic.api.skills.ITargetedEntitySkill
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import kr.toxicity.advancedskills.manager.EntityManager

class EffectEntityMechanic(mythicLineConfig: MythicLineConfig): ISkillMechanic, INoTargetSkill, ITargetedEntitySkill {

    private val entity = mythicLineConfig.getString(arrayOf("entity", "e"))

    override fun castAtEntity(p0: SkillMetadata?, p1: AbstractEntity?): SkillResult {
        val target = (p1 ?: return SkillResult.ERROR).bukkitEntity
        val config = (EntityManager.config(entity) ?: return SkillResult.ERROR)
        config.create(EntityManager.entity(target))
        return SkillResult.SUCCESS
    }

    override fun cast(p0: SkillMetadata?): SkillResult {
        val caster = (p0 ?: return SkillResult.ERROR).caster.entity.bukkitEntity
        val config = (EntityManager.config(entity) ?: return SkillResult.ERROR)
        config.create(EntityManager.entity(caster))
        return SkillResult.SUCCESS
    }
}