package kr.toxicity.advancedskills.entity

import kr.toxicity.advancedskills.api.nms.VirtualItemDisplay
import kr.toxicity.advancedskills.equation.EquationLocation
import kr.toxicity.advancedskills.util.PLUGIN
import kr.toxicity.advancedskills.util.ifNull
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack

class ItemDisplaySupplier(section: ConfigurationSection): DisplaySupplier<VirtualItemDisplay>(
    section,
    {
        PLUGIN.nms().createItem(it)
    }
) {

    private val item = ItemStack(section.getString("material").ifNull("material value not set.").let {
        Material.valueOf(it.uppercase())
    }).apply {
        itemMeta = itemMeta?.apply {
            setCustomModelData(section.getInt("data").coerceAtLeast(0))
        }
    }

    override fun supply(
        parent: SkillEntity,
        count: Int,
        equationLocation: EquationLocation,
        tList: VirtualItemDisplay
    ): SkillEntity {
        return super.supply(parent, count, equationLocation, tList.apply {
            item(item)
        })
    }
}