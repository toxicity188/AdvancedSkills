package kr.toxicity.advancedskills.manager

import kr.toxicity.advancedskills.animation.Animation
import kr.toxicity.advancedskills.pack.PackData
import kr.toxicity.advancedskills.util.*
import net.kyori.adventure.key.Key
import team.unnamed.creative.base.Writable
import team.unnamed.creative.font.Font
import team.unnamed.creative.font.FontProvider
import team.unnamed.creative.texture.Texture
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

object AnimationManager: SkillsManager {

    private const val ANIMATION = "animation"

    private val animationMap = HashMap<String, Animation>()

    override fun reload(data: PackData) {
        animationMap.clear()
        val animationFolder = data.dataFolder.subFolder("animations")
        val pathLength = animationFolder.path.length + 1
        val animationKey = Key.key(PLUGIN.namespace(), ANIMATION)
        var index = 0xC0000
        val font = Font.font()
            .key(animationKey)
        animationFolder.forEach {
            if (!it.isDirectory) return@forEach
            val list = ArrayList<String>()
            it.forEach process@ { target ->
                if (target.extension != "png") return@process warn("This is not a png: ${target.path}")
                val imageKey = Key.key(PLUGIN.namespace(), "${it.path.substring(pathLength, it.path.length)}/${target.name}")
                val image = ImageIO.read(target)
                val string = (index++).parseChar()
                list.add(string)
                font.addProvider(FontProvider.bitMap(
                    imageKey,
                    image.height,
                    image.height,
                    listOf(string)
                ))
                data.resourcePack.texture(Texture.texture(
                    imageKey,
                    Writable.bytes(ByteArrayOutputStream().use { stream ->
                        ImageIO.write(image, "png", stream)
                        stream
                    }.toByteArray())
                ))
            }
            animationMap[it.name] = Animation(animationKey, list)
        }
        if (animationMap.isNotEmpty()) data.resourcePack.font(font.build())
    }

    fun animation(name: String) = animationMap[name]
}