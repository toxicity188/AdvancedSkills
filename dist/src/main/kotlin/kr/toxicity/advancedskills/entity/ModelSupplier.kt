package kr.toxicity.advancedskills.entity

import com.ticxo.modelengine.api.ModelEngineAPI
import kr.toxicity.advancedskills.equation.EquationLocation
import kr.toxicity.advancedskills.util.PLUGIN
import kr.toxicity.advancedskills.util.ifNull
import org.bukkit.configuration.ConfigurationSection

class ModelSupplier(section: ConfigurationSection): TrackableSupplier {

    private val duration = section.getLong("duration", 1L).coerceAtLeast(1L)

    private val model = section.getString("model").ifNull("model value not found.").let {
        ModelEngineAPI.getBlueprint(it).ifNull("this blueprint doesn't exist: $it")
    }

    override fun supply(parent: SkillEntity, scale: Int, equationLocation: EquationLocation): List<SkillEntity> {
        return (0..<scale).map {
            val entity = PLUGIN.nms().createFakeEntity(parent.world())
            val active = ModelEngineAPI.createActiveModel(model)
            ModelEngineAPI.createModeledEntity(entity).addModel(active, true)
            Trackable.of(entity)
            parent.addChild(
                AnimatedTrackable(
                    duration,
                    parent.world(),
                    Trackable.of(entity)
                ),
                it,
                equationLocation
            )
        }
    }
}