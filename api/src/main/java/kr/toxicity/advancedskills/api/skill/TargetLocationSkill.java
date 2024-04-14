package kr.toxicity.advancedskills.api.skill;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

/**
 * Allows targeting some location.
 */
public interface TargetLocationSkill {
    /**
     * Casts skill with some location.
     * @param entity caster
     * @param location target location
     */
    void cast(@NotNull Entity entity, @NotNull Location location);
}
