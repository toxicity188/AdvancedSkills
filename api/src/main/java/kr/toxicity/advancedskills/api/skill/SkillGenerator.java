package kr.toxicity.advancedskills.api.skill;

import kr.toxicity.advancedskills.api.skill.type.EffectEntitySkill;
import org.jetbrains.annotations.NotNull;

/**
 * Represents skill generator.
 */
public interface SkillGenerator {

    /**
     * Gets effect entity skill from given name.
     * @param name entity's name
     * @return implemented skill
     * @throws RuntimeException if entity not found.
     */
    default @NotNull EffectEntitySkill effectEntity(@NotNull String name) {
        return effectEntity(name, EffectEntitySkill.Orient.LOCATION);
    }

    /**
     * Gets effect entity skill from given name.
     * @param name entity's name
     * @param orient entity's orient.
     * @return implemented skill
     * @throws RuntimeException if entity not found.
     */
    @NotNull
    EffectEntitySkill effectEntity(@NotNull String name, @NotNull EffectEntitySkill.Orient orient);
}
