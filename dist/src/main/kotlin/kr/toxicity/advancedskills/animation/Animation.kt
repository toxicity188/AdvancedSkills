package kr.toxicity.advancedskills.animation

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component

data class Animation(
    val key: Key,
    val animation: List<String>,
): Iterable<Component> {
    override fun iterator(): Iterator<Component> = AnimationIterator(this)
}