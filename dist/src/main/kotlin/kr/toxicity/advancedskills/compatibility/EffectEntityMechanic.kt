package kr.toxicity.advancedskills.compatibility

import io.lumine.mythic.api.adapters.AbstractEntity
import io.lumine.mythic.api.adapters.AbstractLocation
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.INoTargetSkill
import io.lumine.mythic.api.skills.ISkillMechanic
import io.lumine.mythic.api.skills.ITargetedEntitySkill
import io.lumine.mythic.api.skills.ITargetedLocationSkill
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import io.lumine.mythic.bukkit.BukkitAdapter
import kr.toxicity.advancedskills.api.skill.type.EffectEntitySkill
import kr.toxicity.advancedskills.util.PLUGIN

class EffectEntityMechanic(mythicLineConfig: MythicLineConfig): ISkillMechanic, INoTargetSkill, ITargetedEntitySkill, ITargetedLocationSkill {

    private val skill = PLUGIN.skillGenerator().effectEntity(
        mythicLineConfig.getString(arrayOf("entity", "e")),
        if (mythicLineConfig.getBoolean(arrayOf("orient", "o"), false)) EffectEntitySkill.Orient.CASTER else EffectEntitySkill.Orient.LOCATION
    )

    override fun castAtEntity(p0: SkillMetadata?, p1: AbstractEntity?): SkillResult {
        skill.cast(
            (p0 ?: return SkillResult.ERROR).caster.entity.bukkitEntity,
            (p1 ?: return SkillResult.INVALID_TARGET).bukkitEntity
        )
        return SkillResult.SUCCESS
    }

    override fun cast(p0: SkillMetadata?): SkillResult {
        skill.cast(
            (p0 ?: return SkillResult.ERROR).caster.entity.bukkitEntity,
        )
        return SkillResult.SUCCESS
    }

    override fun castAtLocation(p0: SkillMetadata?, p1: AbstractLocation?): SkillResult {
        skill.cast(
            (p0 ?: return SkillResult.ERROR).caster.entity.bukkitEntity,
            BukkitAdapter.adapt((p1 ?: return SkillResult.ERROR))
        )
        return SkillResult.SUCCESS
    }
}