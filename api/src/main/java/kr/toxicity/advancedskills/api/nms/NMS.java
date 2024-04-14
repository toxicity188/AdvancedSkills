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

    @NotNull Entity createFakeEntity(@NotNull World world);
    @NotNull VirtualTextDisplay createText(@NotNull World world);
    @NotNull VirtualItemDisplay createItem(@NotNull World world);
    @NotNull VirtualBlockDisplay createBlock(@NotNull World world);
}
