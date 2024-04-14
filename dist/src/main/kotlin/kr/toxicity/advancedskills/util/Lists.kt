package kr.toxicity.advancedskills.util

import java.util.Collections

fun <T> MutableList<T>.synchronized(): MutableList<T> = Collections.synchronizedList(this)
fun <T> MutableSet<T>.synchronized(): MutableSet<T> = Collections.synchronizedSet(this)

fun <T> MutableIterable<T>.forEachSynchronized(block: (MutableIterator<T> ,T) -> Unit) {
    synchronized(this) {
        val iterator = iterator()
        synchronized(iterator) {
            while (iterator.hasNext()) {
                block(iterator, iterator.next())
            }
        }
    }
}