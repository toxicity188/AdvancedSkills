package kr.toxicity.advancedskills.entity

import kr.toxicity.advancedskills.api.nms.VirtualTextDisplay
import kr.toxicity.advancedskills.equation.EquationLocation
import kr.toxicity.advancedskills.manager.AnimationManager
import kr.toxicity.advancedskills.util.PLUGIN
import kr.toxicity.advancedskills.util.ifNull
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Display

class TextDisplaySupplier(section: ConfigurationSection): DisplaySupplier<VirtualTextDisplay>(
    section,
    {
        PLUGIN.nms().createText(it)
    },
    Display.Billboard.CENTER
) {
    private val animation = section.getString("animation")?.let {
        AnimationManager.animation(it).ifNull("Unable to find that animation: $it")
    }

    private val color = section.getString("color")?.let { s ->
        if (s.startsWith('#') && s.length == 7) TextColor.fromHexString(s)
        else NamedTextColor.NAMES.value(s)
    } ?: NamedTextColor.WHITE

    override fun supply(
        parent: SkillEntity,
        count: Int,
        equationLocation: EquationLocation,
        tList: VirtualTextDisplay
    ): SkillEntity {
        val supply = super.supply(parent, count, equationLocation, tList)
        animation?.let {
            val iterator = it.iterator()
            supply.addTickTask {
                if (iterator.hasNext()) tList.text(iterator.next().color(color))
            }
        }
        return supply
    }
}