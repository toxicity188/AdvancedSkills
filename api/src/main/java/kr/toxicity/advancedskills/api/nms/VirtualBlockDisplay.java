package kr.toxicity.advancedskills.api.nms;

import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

/**
 * Represents block display.
 */
public interface VirtualBlockDisplay extends VirtualDisplay {
    /**
     * Sets target block.
     * @see org.bukkit.Material
     * @param data block data
     */
    void block(@NotNull BlockData data);
}
