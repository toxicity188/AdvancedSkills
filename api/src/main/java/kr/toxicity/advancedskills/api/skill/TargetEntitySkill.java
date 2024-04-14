package kr.toxicity.advancedskills.api.skill;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

/**
 * Allows targeting some entity.
 */
public interface TargetEntitySkill {
    /**
     * Casts skill with some target.
     * @param caster caster
     * @param target target
     */
    void cast(@NotNull Entity caster, @NotNull Entity target);
}
