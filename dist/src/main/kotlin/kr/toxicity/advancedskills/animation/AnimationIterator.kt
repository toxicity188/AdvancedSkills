package kr.toxicity.advancedskills.animation

import net.kyori.adventure.text.Component

class AnimationIterator(
    private val animation: Animation
): Iterator<Component> {
    private var i = 0

    override fun hasNext() = i < animation.animation.size
    override fun next() = Component.text()
        .content(animation.animation[i++])
        .font(animation.key)
        .build()
}