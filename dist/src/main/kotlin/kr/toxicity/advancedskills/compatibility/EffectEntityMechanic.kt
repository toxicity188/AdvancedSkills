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
import kr.toxicity.advancedskills.entity.SkillEntity
import kr.toxicity.advancedskills.manager.EntityManager

class EffectEntityMechanic(mythicLineConfig: MythicLineConfig): ISkillMechanic, INoTargetSkill, ITargetedEntitySkill, ITargetedLocationSkill {

    private val entity = mythicLineConfig.getString(arrayOf("entity", "e"))
    private val orient = mythicLineConfig.getBoolean(arrayOf("orient", "o"), true)

    override fun castAtEntity(p0: SkillMetadata?, p1: AbstractEntity?): SkillResult {
        val config = (EntityManager.config(entity) ?: return SkillResult.ERROR)
        val target = EntityManager.entity((p1 ?: return SkillResult.INVALID_TARGET).bukkitEntity)
        config.create(if (orient) target else target.locationEntity())
        return SkillResult.SUCCESS
    }

    override fun cast(p0: SkillMetadata?): SkillResult {
        val caster = (p0 ?: return SkillResult.ERROR).caster()
        val config = (EntityManager.config(entity) ?: return SkillResult.ERROR)
        config.create(if (orient) caster else caster.locationEntity())
        return SkillResult.SUCCESS
    }

    override fun castAtLocation(p0: SkillMetadata?, p1: AbstractLocation?): SkillResult {
        val config = (EntityManager.config(entity) ?: return SkillResult.ERROR)
        config.create(if (orient) (p0 ?: return SkillResult.ERROR).caster() else {
            val loc = BukkitAdapter.adapt((p1 ?: return SkillResult.ERROR))
            EntityManager.location(loc.world ?: return SkillResult.ERROR, loc)
        })
        return SkillResult.SUCCESS
    }

    private fun SkillMetadata.caster(): SkillEntity = EntityManager.entity(caster.entity.bukkitEntity)
}