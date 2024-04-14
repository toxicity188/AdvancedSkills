package kr.toxicity.advancedskills.manager

import kr.toxicity.advancedskills.pack.PackData
import kr.toxicity.advancedskills.util.PLUGIN
import kr.toxicity.advancedskills.util.parseChar
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import team.unnamed.creative.font.Font
import team.unnamed.creative.font.FontProvider

object SpaceManager: SkillsManager {

    private const val CENTER_VALUE = 0xD0000
    private val range = -1024..1024
    private var key = Key.key(PLUGIN.namespace(), "space")

    override fun reload(data: PackData) {
        val spaceFont = Key.key(PLUGIN.namespace(), "space")
        val font = Font.font()
            .key(spaceFont)
        key = spaceFont
        val provider = FontProvider.space()
        range.forEach {
            provider.advance((CENTER_VALUE + it).parseChar(), it)
        }
        data.resourcePack.font(font
            .addProvider(provider.build())
            .build())
    }

    fun space(space: Int) = Component.text()
        .content((CENTER_VALUE + space).parseChar())
        .font(key)
        .build()
}