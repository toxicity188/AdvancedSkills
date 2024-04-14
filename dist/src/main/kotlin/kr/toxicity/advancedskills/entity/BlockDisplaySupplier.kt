package kr.toxicity.advancedskills.entity

import kr.toxicity.advancedskills.api.nms.VirtualBlockDisplay
import kr.toxicity.advancedskills.equation.EquationLocation
import kr.toxicity.advancedskills.util.PLUGIN
import kr.toxicity.advancedskills.util.ifNull
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection

class BlockDisplaySupplier(section: ConfigurationSection): DisplaySupplier<VirtualBlockDisplay>(
    section,
    {
        PLUGIN.nms().createBlock(it)
    }
) {
    private val material = section.getString("material").ifNull("material value not set.").let {
        Material.valueOf(it.uppercase())
    }.apply {
        if (!isBlock) throw RuntimeException("this is a not block: $this")
    }.createBlockData()

    override fun supply(
        parent: SkillEntity,
        count: Int,
        equationLocation: EquationLocation,
        tList: VirtualBlockDisplay
    ): SkillEntity {
        return super.supply(parent, count, equationLocation, tList.apply {
            block(material)
        })
    }
}