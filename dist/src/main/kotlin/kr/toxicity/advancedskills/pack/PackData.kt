package kr.toxicity.advancedskills.pack

import kr.toxicity.advancedskills.util.DATA_FOLDER
import kr.toxicity.advancedskills.util.PLUGIN
import net.kyori.adventure.text.Component
import team.unnamed.creative.ResourcePack
import team.unnamed.creative.metadata.pack.PackMeta

class PackData {
    val resourcePack = ResourcePack.resourcePack().apply {
        packMeta(PackMeta.of(
            PLUGIN.nms().version().metaVersion,
            Component.text("AdvancedSkills's resource pack!")
        ))
    }
    val dataFolder = DATA_FOLDER
}