package kr.toxicity.advancedskills.equation

import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import kotlin.math.E
import kotlin.math.PI

class TEquation(string: String, parameterCount: Int = 1) {
    private val expression = ExpressionBuilder(string)
        .variables(mutableSetOf(
            "pi",
            "e"
        ).apply {
            (0..<parameterCount).forEach {
                add("t${if (it == 0) "" else (it + 1)}")
            }
        })
        .build()

    fun evaluate(vararg double: Double): Double {
        val map = mutableMapOf(
            "pi" to PI,
            "e" to E
        )
        double.forEachIndexed { index, d ->
            map["t${if (index == 0) "" else (index + 1)}"] = d
        }
        return Expression(expression)
            .setVariables(map)
            .evaluate()
    }
}