package kr.toxicity.advancedskills.api.nms;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

/**
 * Volatile code handler.
 */
public interface NMS {
    /**
     * Gets server's version.
     * @return server version
     */
    @NotNull NMSVersion version();

    /**
     * Creates fake entity by given world.
     * @param world target world
     * @return fake instance of entity
     */
    @NotNull Entity createFakeEntity(@NotNull World world);

    /**
     * Creates virtual text display that not spawned in server.
     * @param world target world.
     * @return virtual entity
     */
    @NotNull VirtualTextDisplay createText(@NotNull World world);

    /**
     * Creates virtual item display that not spawned in server.
     * @param world target world.
     * @return virtual entity
     */
    @NotNull VirtualItemDisplay createItem(@NotNull World world);


    /**
     * Creates virtual block display that not spawned in server.
     * @param world target world.
     * @return virtual entity
     */
    @NotNull VirtualBlockDisplay createBlock(@NotNull World world);
}
