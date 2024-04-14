package kr.toxicity.advancedskills.api.nms;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Display;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Represents display entity.
 */
public interface VirtualDisplay {
    /**
     * Gets the uuid.
     * @return uuid
     */
    @NotNull
    UUID uuid();

    /**
     * Gets current location.
     * @return entity's location
     */
    @NotNull
    Location location();

    /**
     * Gets current world.
     * @return entity's world
     */
    @NotNull
    World world();

    /**
     * Gets whether remove() is called or not.
     * @return is removed or not
     */
    boolean isRemoved();

    /**
     * Teleports entity by some location
     * @param location target location
     */
    void teleport(@NotNull Location location);

    /**
     * Sets the scale of entity
     * @param vector scale
     */
    void scale(@NotNull Vector vector);

    /**
     * Sets the brightness of display.
     * @param brightness brightness
     */
    void brightness(@NotNull Display.Brightness brightness);

    /**
     * Sets the billboard of display.
     * @param billboard billboard
     */
    void billboard(@NotNull Display.Billboard billboard);

    /**
     * Removes and disable this entity.
     */
    void remove();
}
