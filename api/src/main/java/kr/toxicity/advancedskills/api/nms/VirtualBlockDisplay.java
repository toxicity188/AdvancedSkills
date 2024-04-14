package kr.toxicity.advancedskills.api.nms;

import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

public interface VirtualBlockDisplay extends VirtualDisplay {
    void block(@NotNull BlockData data);
}
