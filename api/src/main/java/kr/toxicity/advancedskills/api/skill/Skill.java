package kr.toxicity.advancedskills.api.skill;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

/**
 * Represents skill.
 */
public interface Skill {
    /**
     * Casts skill with no target.
     * @param entity caster
     */
    void cast(@NotNull Entity entity);
}
